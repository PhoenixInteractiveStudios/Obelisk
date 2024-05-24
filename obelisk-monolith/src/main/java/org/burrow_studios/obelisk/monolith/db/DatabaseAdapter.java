package org.burrow_studios.obelisk.monolith.db;

import org.burrow_studios.obelisk.monolith.action.DatabaseAction;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseAdapter {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Set<IActionableDatabase> listeners = ConcurrentHashMap.newKeySet();

    public <T> @NotNull CompletableFuture<T> submit(@NotNull DatabaseAction<T> action) {
        CompletableFuture<T> future = new CompletableFuture<>();

        executor.submit(() -> {
            try {
                this.execute(action, future);
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });

        return future;
    }

    private <T> void execute(@NotNull DatabaseAction<T> action, @NotNull CompletableFuture<T> future) throws DatabaseException {
        for (IActionableDatabase listener : listeners)
            action.execute(listener, future);
    }

    public void registerDatabase(@NotNull IActionableDatabase database) {
        this.listeners.add(database);
    }
}
