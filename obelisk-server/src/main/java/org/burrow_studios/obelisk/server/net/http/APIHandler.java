package org.burrow_studios.obelisk.server.net.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.burrow_studios.obelisk.server.net.http.exceptions.ForbiddenException;
import org.burrow_studios.obelisk.server.net.http.exceptions.IllegalMethodException;
import org.burrow_studios.obelisk.server.net.http.exceptions.UnauthorizedException;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class APIHandler {
    protected final @NotNull NetworkHandler networkHandler;

    protected static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private final ConcurrentHashMap<Endpoint, EndpointHandler> handlers;

    public APIHandler(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
        this.handlers = new ConcurrentHashMap<>();
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

    protected final @NotNull Response handle(@NotNull Map<String, String> headers, @NotNull Method method, @NotNull String path) {
        final String[] segments = path.substring(1).split("/");

        Endpoint endpoint = null;

        for (Endpoint e : this.handlers.keySet()) {
            if (!Objects.equals(e.getMethod(), method)) continue;
            if (!e.matchPath(segments))                 continue;

            endpoint = e;
            break;
        }

        if (endpoint == null)
            endpoint = new Endpoint(method, path);

        EndpointHandler handler = this.handlers.get(endpoint);

        if (handler == null) {
            // no handler found. Looking for endpoints with the same path but different methods

            final List<String> methods = this.handlers.keySet().stream()
                    // filter for matching paths
                    .filter(e -> e.matchPath(segments))
                    // get other methods of same path
                    .map(Endpoint::getMethod)
                    .map(Enum::name)
                    .toList();

            if (!methods.isEmpty()) {
                // there seem to be other methods, this calls for a 405
                return new ResponseBuilder()
                        .setCode(405)
                        .setHeader("Allow", String.join(", ", methods))
                        .build();
            }

            // path does not exist on the server at all -> 404
            return new ResponseBuilder()
                    .setCode(404)
                    .build();
        }

        final Request request = new Request(
                this,
                endpoint,
                headers
        );

        try {
            final ResponseBuilder builder = new ResponseBuilder();

            handler.handle(request, builder);

            return builder.build();
        } catch (UnauthorizedException e) {
            return new ResponseBuilder()
                    .setCode(401)
                    .setHeader("WWW-Authenticate", "Bearer")
                    .build();
        } catch (ForbiddenException e) {
            return new ResponseBuilder()
                    .setCode(403)
                    .build();
        } catch (IllegalMethodException e) {
            return new ResponseBuilder()
                    .setCode(405)
                    .setHeader("Allow", String.join(", ", e.getAllowedStr()))
                    .build();
        } catch (Exception e) {
            return new ResponseBuilder()
                    .setCode(500)
                    .build();
        }
    }
}
