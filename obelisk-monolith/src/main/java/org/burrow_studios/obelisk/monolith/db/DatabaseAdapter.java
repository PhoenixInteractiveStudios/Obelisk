package org.burrow_studios.obelisk.monolith.db;

import org.burrow_studios.obelisk.monolith.action.DatabaseAction;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseAdapter {
    private final ExecutorService executor = Executors.newCachedThreadPool();
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

    @SuppressWarnings("unchecked")
    private <T> void execute(@NotNull DatabaseAction<T> action, @NotNull CompletableFuture<T> future) {
        Method method = null;
        for (Method m : IActionableDatabase.class.getMethods()) {
            Class<?>[] params = m.getParameterTypes();

            if (params.length != 1) continue;
            if (!params[0].isAssignableFrom(action.getClass())) continue;

            method = m;
            break;
        }

        if (method == null)
            throw new Error("Not implemented");

        for (IActionableDatabase listener : listeners) {
            try {
                Object res = method.invoke(listener, action);

                if (res != null) {
                    future.complete((T) res);
                    return;
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new Error("Implementation error", e);
            } catch (InvocationTargetException e) {
                Throwable ex = e.getTargetException();
                future.completeExceptionally(ex);
                return;
            }
        }

        if (!future.isDone())
            future.complete(null);
    }

    public void registerDatabase(@NotNull IActionableDatabase database) {
        this.listeners.add(database);
    }
}
