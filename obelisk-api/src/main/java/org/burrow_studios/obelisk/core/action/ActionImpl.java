package org.burrow_studios.obelisk.core.action;

import com.google.gson.JsonElement;
import org.burrow_studios.obelisk.api.action.Action;
import org.burrow_studios.obelisk.commons.function.ExceptionalBiFunction;
import org.burrow_studios.obelisk.commons.rpc.Method;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.*;

public class ActionImpl<T> implements Action<T> {
    protected final @NotNull ObeliskImpl api;
    private final @NotNull ExceptionalBiFunction<RPCRequest, RPCResponse, T, ? extends Exception> mapper;

    private final @NotNull Method method;
    private final @NotNull String uri;

    public ActionImpl(
            @NotNull ObeliskImpl api,
            @NotNull Method method,
            @NotNull String uri,
            @NotNull ExceptionalBiFunction<RPCRequest, RPCResponse, T, ? extends Exception> mapper
    ) {
        this.api = api;
        this.method = method;
        this.uri = uri;
        this.mapper = mapper;
    }

    public ActionImpl(
            @NotNull ObeliskImpl api,
            @NotNull T returnValue,
            @NotNull Method method,
            @NotNull String uri
    ) {
        this(api, method, uri, (request, response) -> returnValue);
    }

    @Override
    public final @NotNull ObeliskImpl getAPI() {
        return this.api;
    }

    public final @NotNull RPCRequest.Builder requestBuilder() {
        RPCRequest.Builder builder = new RPCRequest.Builder();
        builder.setMethod(method);
        builder.setPath(uri);
        builder.setBody(getContent());
        return builder;
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
        return this.api.getDataProvider().submitRequest(this, (request1, response) -> {
            try {
                return mapper.apply(request1, response);
            } catch (Exception e) {
                throw new CompletionException(e);
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
