package org.burrow_studios.obelisk.monolith.db;

import org.burrow_studios.obelisk.monolith.action.DatabaseAction;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DatabaseAdapter {
    public <T> @NotNull CompletableFuture<T> submit(@NotNull DatabaseAction<T> action) {
        // TODO
        return CompletableFuture.failedFuture(new Error("Not implemented"));
    }
}
