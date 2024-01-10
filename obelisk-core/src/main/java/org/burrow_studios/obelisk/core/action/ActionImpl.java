package org.burrow_studios.obelisk.core.action;

import com.google.gson.JsonElement;
import org.burrow_studios.obelisk.api.action.Action;
import org.burrow_studios.obelisk.util.function.ExceptionalBiFunction;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.net.NetworkHandler;
import org.burrow_studios.obelisk.core.net.Request;
import org.burrow_studios.obelisk.core.net.Response;
import org.burrow_studios.obelisk.core.net.http.CompiledRoute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ActionImpl<T> implements Action<T> {
    protected final @NotNull ObeliskImpl api;
    private final @NotNull CompiledRoute route;
    private final @NotNull ExceptionalBiFunction<Request, Response, T, ? extends Exception> mapper;

    public ActionImpl(
            @NotNull ObeliskImpl api,
            @NotNull CompiledRoute route,
            @NotNull ExceptionalBiFunction<Request, Response, T, ? extends Exception> mapper
    ) {
        this.api = api;
        this.route = route;
        this.mapper = mapper;
    }

    public ActionImpl(
            @NotNull ObeliskImpl api,
            @NotNull T returnValue,
            @NotNull CompiledRoute route
    ) {
        this(api, route, (request, response) -> returnValue);
    }

    @Override
    public final @NotNull ObeliskImpl getAPI() {
        return this.api;
    }

    public final @NotNull CompiledRoute getRoute() {
        return route;
    }

    public @Nullable JsonElement getContent() {
        return null;
    }

    @Override
    public final void queue() {
        this.submit();
    }

    @Override
    public final @NotNull CompletableFuture<T> submit() {
        final NetworkHandler networkHandler = this.api.getNetworkHandler();
        final Request        request        = networkHandler.submitRequest(this);

        return request.getFuture().handle((response, throwable) -> {
            try {
                return this.mapper.apply(request, response);
            } catch (Exception e) {
                // TODO: handle
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public final T await(long timeout, @NotNull TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        return this.submit().get(timeout, unit);
    }

    @Override
    public final T await() throws ExecutionException, InterruptedException {
        return this.submit().get();
    }
}
