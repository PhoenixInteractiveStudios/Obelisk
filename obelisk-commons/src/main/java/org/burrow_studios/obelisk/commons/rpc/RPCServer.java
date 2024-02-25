package org.burrow_studios.obelisk.commons.rpc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.burrow_studios.obelisk.commons.rpc.exceptions.RequestHandlerException;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class RPCServer<T extends RPCServer<T>> implements Closeable {
    static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    /** Maps expected endpoints to their respective handlers. This serves as lookup during request processing. */
    private final ConcurrentHashMap<Endpoint, EndpointHandler> handlers = new ConcurrentHashMap<>();

    /**
     * Registers an {@link EndpointHandler} as the responsible handler for a specific {@link Endpoint}.
     * @return This RPCServer instance for chaining purposes.
     */
    public final @NotNull RPCServer<T> addEndpoint(@NotNull Endpoint endpoint, @NotNull EndpointHandler handler) {
        this.handlers.put(endpoint, handler);
        return this;
    }

    /**
     * Removes and {@link Endpoint} from the internal registry, causing this server to "forget" any mapped
     * {@link EndpointHandler}.
     * @return This RPCServer instance for chaining purposes.
     */
    public final @NotNull RPCServer<T> removeEndpoint(@NotNull Endpoint endpoint) {
        this.handlers.remove(endpoint);
        return this;
    }

    public final @NotNull Set<Endpoint> getEndpoints() {
        return this.handlers.keySet();
    }

    protected final @NotNull RPCResponse handle(@NotNull RPCRequest request) throws IOException {
        String[] segments = request.getPath().substring(1).split("/");

        Endpoint endpoint = null;
        for (Endpoint e : this.handlers.keySet()) {
            if (e.getMethod() != request.getMethod()) continue;
            if (!e.match(segments))                   continue;

            endpoint = e;
            break;
        }

        final EndpointHandler handler = Optional.ofNullable(endpoint)
                .map(this.handlers::get)
                .orElse(null);

        if (handler == null) {
            // no handler found. Looking for endpoints with the same path but different methods

            final List<String> methods = this.handlers.keySet().stream()
                    // filter for matching paths
                    .filter(e -> e.match(segments))
                    // get other methods of same path
                    .map(Endpoint::getMethod)
                    .map(Enum::name)
                    .toList();

            if (!methods.isEmpty()) {
                // there seem to be other methods
                return new RPCResponse.Builder(request)
                        .setStatus(Status.METHOD_NOT_ALLOWED)
                        .setHeader("Allow", String.join(", ", methods))
                        .build();
            }

            // path does not exist on the server at all
            return new RPCResponse.Builder(request)
                    .setStatus(Status.NOT_FOUND)
                    .build();
        }

        // TODO: auth

        try {
            RPCResponse.Builder builder = new RPCResponse.Builder(request);
            handler.handle(request, builder);
            return builder.build();
        } catch (RequestHandlerException e) {
            return e.asResponse(request);
        } catch (Exception e) {
            return new RPCResponse.Builder(request)
                    .setStatus(Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
