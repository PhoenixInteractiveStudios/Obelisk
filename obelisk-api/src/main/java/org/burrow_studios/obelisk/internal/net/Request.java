package org.burrow_studios.obelisk.internal.net;

import com.google.gson.JsonElement;
import org.burrow_studios.obelisk.internal.net.http.CompiledRoute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public final class Request {
    private final @NotNull NetworkHandler networkHandler;
    private final long id;

    private final @NotNull CompiledRoute route;
    private final @Nullable JsonElement content;
    private final @NotNull TimeoutContext timeout;

    private final @NotNull CompletableFuture<Response> future;

    Request(
            @NotNull NetworkHandler networkHandler,
            long id,
            @NotNull CompiledRoute route,
            @Nullable JsonElement content,
            @NotNull TimeoutContext timeout
    ) {
        this.networkHandler = networkHandler;
        this.id = id;

        this.route = route;
        this.content = content;
        this.timeout = timeout;

        this.future = new CompletableFuture<>();
    }

    public @NotNull NetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    public long getId() {
        return id;
    }

    public @NotNull CompiledRoute getRoute() {
        return route;
    }

    public @Nullable JsonElement getContent() {
        return content;
    }

    public @NotNull TimeoutContext getTimeout() {
        return timeout;
    }

    public @NotNull CompletableFuture<Response> getFuture() {
        return future;
    }
}
