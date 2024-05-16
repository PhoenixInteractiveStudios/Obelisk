package org.burrow_studios.obelisk.client.http;

import com.google.gson.JsonElement;
import org.burrow_studios.obelisk.api.action.TimeoutContext;
import org.burrow_studios.obelisk.client.action.ActionImpl;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class Request<T> {
    private final ActionImpl<T> action;
    private final Consumer<? super T> onSuccess;
    private final Consumer<? super Throwable> onFailure;
    private final Route.Compiled route;
    private final JsonElement body;
    private final Map<String, String> headers;
    private final TimeoutContext timeout;

    private final AtomicBoolean done = new AtomicBoolean(false);
    private final AtomicBoolean cancelled = new AtomicBoolean(false);

    public Request(
            @NotNull ActionImpl<T> action,
            @NotNull Consumer<? super T> onSuccess,
            @NotNull Consumer<? super Throwable> onFailure,
            @NotNull Route.Compiled route,
            @Nullable JsonElement body,
            @NotNull Map<String, String> headers,
            @NotNull TimeoutContext timeout
    ) {
        this.action = action;
        this.onSuccess = onSuccess;
        this.onFailure = onFailure;
        this.route = route;
        this.body = body;
        this.headers = headers;
        this.timeout = timeout;
    }

    public void onSuccess(T result) {
        if (done.getAndSet(true)) return;

        this.onSuccess.accept(result);
    }

    public void onFailure(Throwable t) {
        if (done.getAndSet(true)) return;

        this.onFailure.accept(t);
    }

    public @NotNull Consumer<? super T> getOnSuccess() {
        return this.onSuccess;
    }

    public @NotNull Consumer<? super Throwable> getOnFailure() {
        return this.onFailure;
    }

    public @NotNull Route.Compiled getRoute() {
        return this.route;
    }

    public @Nullable JsonElement getBody() {
        return this.body;
    }

    public @NotNull Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(this.headers);
    }

    public @NotNull TimeoutContext getTimeout() {
        return timeout;
    }

    public boolean isDone() {
        return this.done.get();
    }

    public boolean isCancelled() {
        return this.cancelled.get();
    }

    public void cancel() {
        this.cancelled.set(true);
    }
}
