package org.burrow_studios.obelisk.monolith.db;

import org.burrow_studios.obelisk.monolith.action.DatabaseAction;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseAdapter {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final IActionableDatabase database;

    public DatabaseAdapter(@NotNull IActionableDatabase database) {
        this.database = database;
    }

    public <T> @NotNull CompletableFuture<T> submit(@NotNull DatabaseAction<T> action) {
        CompletableFuture<T> future = new CompletableFuture<>();

        executor.submit(() -> {
            try {
                action.execute(database, future);
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });

        return future;
    }
}
