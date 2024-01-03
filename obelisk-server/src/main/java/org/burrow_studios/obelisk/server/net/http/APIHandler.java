package org.burrow_studios.obelisk.server.net.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.burrow_studios.obelisk.server.net.http.exceptions.ForbiddenException;
import org.burrow_studios.obelisk.server.net.http.exceptions.IllegalMethodException;
import org.burrow_studios.obelisk.server.net.http.exceptions.UnauthorizedException;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class APIHandler {
    protected final @NotNull NetworkHandler networkHandler;

    protected static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private final ConcurrentHashMap<Endpoint, EndpointHandler> handlers;
    private final EndpointHandler defaultHandler;

    public APIHandler(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
        this.handlers = new ConcurrentHashMap<>();
        this.defaultHandler = (req, res) -> res.setCode(404);
    }

    public final @NotNull APIHandler addEndpoint(@NotNull Method method, @NotNull String path, @NotNull EndpointHandler handler) {
        return this.addEndpoint(new Endpoint(method, path), handler);
    }

    public final @NotNull APIHandler addEndpoint(@NotNull Endpoint endpoint, @NotNull EndpointHandler handler) {
        this.handlers.put(endpoint, handler);
        return this;
    }

    public final @NotNull APIHandler removeEndpoint(@NotNull Endpoint endpoint) {
        this.handlers.remove(endpoint);
        return this;
    }

    public final @NotNull NetworkHandler getNetworkHandler() {
        return this.networkHandler;
    }

    protected final @NotNull Endpoint getEndpoint(@NotNull Method method, @NotNull String path) {
        final String[] segments = path.substring(1).split("/");

        for (Endpoint endpoint : handlers.keySet()) {
            if (!Objects.equals(endpoint.getMethod(), method)) continue;
            if (!endpoint.matchPath(segments))                 continue;

            return endpoint;
        }

        return new Endpoint(method, path);
    }

    protected final @NotNull EndpointHandler getHandler(@NotNull Endpoint endpoint) {
        return this.handlers.getOrDefault(endpoint, this.defaultHandler);
    }

    protected final @NotNull Response handle(@NotNull Map<String, String> headers, @NotNull Method method, @NotNull String path) {
        final Endpoint        endpoint = this.getEndpoint(method, path);
        final EndpointHandler handler  = this.getHandler(endpoint);

        final Request request = new Request(
                this,
                endpoint,
                headers
        );

        final ResponseBuilder builder = new ResponseBuilder();
        final ResponseBuilder error   = new ResponseBuilder();

        try {
            handler.handle(request, builder);

            return builder.build();
        } catch (UnauthorizedException e) {
            error.setCode(401);

            error.setHeader("WWW-Authenticate", "Bearer");
        } catch (ForbiddenException e) {
            error.setCode(403);
        } catch (IllegalMethodException e) {
            error.setCode(405);

            error.setHeader("Allow", String.join(", ", e.getAllowedStr()));
        } catch (Exception e) {
            error.setCode(500);
        }

        return error.build();
    }
}
