package org.burrow_studios.obelisk.commons.rpc;

import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface RPCClient extends Closeable {
    @NotNull CompletableFuture<RPCResponse> send(@NotNull RPCRequest request) throws IOException;
}
