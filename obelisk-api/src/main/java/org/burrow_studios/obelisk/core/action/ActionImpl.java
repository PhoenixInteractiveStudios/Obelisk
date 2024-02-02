package org.burrow_studios.obelisk.core.action;

import com.google.gson.JsonElement;
import org.burrow_studios.obelisk.api.action.Action;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.source.Request;
import org.burrow_studios.obelisk.core.source.Response;
import org.burrow_studios.obelisk.core.net.http.CompiledEndpoint;
import org.burrow_studios.obelisk.core.source.DataProvider;
import org.burrow_studios.obelisk.common.function.ExceptionalBiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ActionImpl<T> implements Action<T> {
    protected final @NotNull ObeliskImpl api;
    private final @NotNull CompiledEndpoint endpoint;
    private final @NotNull ExceptionalBiFunction<Request, Response, T, ? extends Exception> mapper;

    public ActionImpl(
            @NotNull ObeliskImpl api,
            @NotNull CompiledEndpoint endpoint,
            @NotNull ExceptionalBiFunction<Request, Response, T, ? extends Exception> mapper
    ) {
        this.api = api;
        this.endpoint = endpoint;
        this.mapper = mapper;
    }

    public ActionImpl(
            @NotNull ObeliskImpl api,
            @NotNull T returnValue,
            @NotNull CompiledEndpoint endpoint
    ) {
        this(api, endpoint, (request, response) -> returnValue);
    }

    @Override
    public final @NotNull ObeliskImpl getAPI() {
        return this.api;
    }

    public final @NotNull CompiledEndpoint getEndpoint() {
        return endpoint;
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
        final DataProvider source  = this.api.getDataProvider();
        final Request      request = source.submitRequest(this);

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