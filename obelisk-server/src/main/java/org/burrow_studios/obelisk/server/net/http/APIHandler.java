package org.burrow_studios.obelisk.server.net.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
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

    protected final @NotNull Endpoint getEndpoint(@NotNull Method method, @NotNull URI uri) {
        final String   path     = uri.getPath();
        final String[] segments = path.substring(1).split("/");

        for (Endpoint endpoint : handlers.keySet()) {
            if (!Objects.equals(endpoint.getMethod(), method)) continue;
            if (!endpoint.matchPath(segments))                 continue;

            return endpoint;
        }

        return new Endpoint(method, uri.getPath());
    }

    protected final @NotNull EndpointHandler getHandler(@NotNull Endpoint endpoint) {
        return this.handlers.getOrDefault(endpoint, this.defaultHandler);
    }
}
