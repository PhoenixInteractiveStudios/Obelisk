package org.burrow_studios.obelisk.monolith.action;

import org.burrow_studios.obelisk.api.action.Action;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.db.interfaces.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class DatabaseAction<T> implements Action<T> {
    private final ObeliskMonolith obelisk;

    protected DatabaseAction(@NotNull ObeliskMonolith obelisk) {
        this.obelisk = obelisk;
    }

    @Override
    public final @NotNull ObeliskMonolith getAPI() {
        return this.obelisk;
    }

    @Override
    public void queue() {
        this.submit();
    }

    @Override
    public @NotNull CompletableFuture<T> submit() {
        return this.getAPI().getDatabaseAdapter().submit(this);
    }

    @Override
    public T await(long timeout, @NotNull TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        // TODO: cancel action
        return this.submit().get(timeout, unit);
    }

    @Override
    public T await() throws ExecutionException, InterruptedException {
        return this.submit().get();
    }

    public abstract void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<T> future) throws DatabaseException;
}
