package org.burrow_studios.obelisk.core.source;

import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.ActionImpl;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public interface DataProvider {
    @NotNull ObeliskImpl getAPI();

    <T> CompletableFuture<T> submitRequest(@NotNull ActionImpl<?> action, @NotNull BiFunction<RPCRequest, RPCResponse, T> mapper);
}
