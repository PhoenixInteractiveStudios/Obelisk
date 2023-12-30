package org.burrow_studios.obelisk.internal.action;

import org.burrow_studios.obelisk.api.action.Action;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class ActionImpl<T> implements Action<T> {
    protected final @NotNull ObeliskImpl api;

    protected ActionImpl(@NotNull ObeliskImpl api) {
        this.api = api;
    }

    @Override
    public final @NotNull ObeliskImpl getAPI() {
        return this.api;
    }

    @Override
    public final void queue() {
        this.submit();
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
