package org.burrow_studios.obelisk.client.action;

import com.google.gson.JsonElement;
import org.burrow_studios.obelisk.api.action.Action;
import org.burrow_studios.obelisk.api.action.TimeoutContext;
import org.burrow_studios.obelisk.api.exceptions.ErrorResponseException;
import org.burrow_studios.obelisk.api.request.ErrorResponse;
import org.burrow_studios.obelisk.client.http.Request;
import org.burrow_studios.obelisk.client.http.Response;
import org.burrow_studios.obelisk.core.AbstractObelisk;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;

public abstract class ActionImpl<T> implements Action<T> {
    protected final @NotNull AbstractObelisk obelisk;
    private final @NotNull Route.Compiled route;

    private final @Nullable BiFunction<Request<T>, Response, T> handler;

    public ActionImpl(@NotNull AbstractObelisk obelisk, @NotNull Route.Compiled route, @Nullable BiFunction<Request<T>, Response, T> handler) {
        this.obelisk = obelisk;
        this.route = route;
        this.handler = handler;
    }

    public ActionImpl(@NotNull AbstractObelisk obelisk, @NotNull Route.Compiled route) {
        this(obelisk, route, (request, response) -> {
            // TODO: check for failure
            return null;
        });
    }

    public static <T> @NotNull ActionImpl<T> simpleGet(@NotNull AbstractObelisk obelisk, @NotNull Route.Compiled route, @Nullable BiFunction<Request<T>, Response, T> handler) {
        return new ActionImpl<T>(obelisk, route, handler) {
            @Override
            protected @Nullable JsonElement getRequestBody() {
                return null;
            }
        };
    }

    @Override
    public final @NotNull AbstractObelisk getAPI() {
        return this.obelisk;
    }

    @Override
    public final void queue() {
        this.submit();
    }

    @Override
    public final @NotNull CompletableFuture<T> submit() {
        Map<String, String> headers = new LinkedHashMap<>(this.getRequestHeaders());
        return new ActionFuture<>(this, route, getRequestBody(), headers, TimeoutContext.DEFAULT);
    }

    @Override
    public final T await(long timeout, @NotNull TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        Map<String, String> headers = new LinkedHashMap<>(this.getRequestHeaders());
        TimeoutContext timeoutCtx = TimeoutContext.timeout(timeout, unit);
        ActionFuture<T> future = new ActionFuture<>(this, route, getRequestBody(), headers, timeoutCtx);
        return future.get(timeout, unit);
    }

    @Override
    public final T await() throws ExecutionException, InterruptedException {
        return this.submit().get();
    }

    public void handleResponse(@NotNull Request<T> request, @NotNull Response response) {
        if (response.isOk()) {
            if (this.handler == null) {
                request.onSuccess(null);
            } else {
                try {
                    request.onSuccess(handler.apply(request, response));
                } catch (Throwable t) {
                    // TODO: should the raw throwable be passed here?
                    request.onFailure(t);

                    if (t instanceof Error)
                        throw t;
                }
            }
        } else {
            request.onFailure(new ErrorResponseException(ErrorResponse.fromCode(response.getCode())));
        }
    }

    protected abstract @Nullable JsonElement getRequestBody();

    // may be overwritten
    protected @NotNull Map<String, String> getRequestHeaders() {
        return Map.of();
    }
}
