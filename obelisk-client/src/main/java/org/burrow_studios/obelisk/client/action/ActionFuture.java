package org.burrow_studios.obelisk.client.action;

import com.google.gson.JsonElement;
import org.burrow_studios.obelisk.api.action.TimeoutContext;
import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.client.http.Request;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ActionFuture<T> extends CompletableFuture<T> {
    private final Request<T> request;

    public ActionFuture(
            @NotNull ActionImpl<T> action,
            @NotNull Route.Compiled route,
            @Nullable JsonElement body,
            @NotNull Map<String, String> headers,
            @NotNull TimeoutContext timeout
    ) {
        this.request = new Request<>(action, this::complete, this::completeExceptionally, route, body, headers, timeout);
        ((ObeliskImpl) action.getAPI()).getHttpClient().request(this.request);
    }

    public ActionFuture(@NotNull T result) {
        this.request = null;
        this.complete(result);
    }

    public ActionFuture(@NotNull Throwable t) {
        this.request = null;
        this.completeExceptionally(t);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (this.request != null)
            this.request.cancel();

        return (!isDone() && !isCancelled()) && super.cancel(mayInterruptIfRunning);
    }
}
