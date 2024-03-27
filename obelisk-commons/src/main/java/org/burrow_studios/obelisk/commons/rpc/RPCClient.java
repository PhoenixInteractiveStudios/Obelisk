package org.burrow_studios.obelisk.commons.rpc;

import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public abstract class RPCClient implements Closeable {
    protected final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @NotNull
    public abstract CompletableFuture<RPCResponse> send(@NotNull RPCRequest request) throws IOException;
}
