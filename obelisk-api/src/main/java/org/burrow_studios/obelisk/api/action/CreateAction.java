package org.burrow_studios.obelisk.api.action;

import org.burrow_studios.obelisk.api.Obelisk;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface CreateAction<T extends Turtle> {
    @NotNull Obelisk getAPI();

    /** Submits this action to be completed asynchronously. */
    void queue();

    /** Submits this action and returns a {@link CompletableFuture} that will be completed  */
    @NotNull CompletableFuture<T> submit();

    default T await(long timeout, @NotNull TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        return this.submit().get(timeout, unit);
    }

    default T await() throws ExecutionException, InterruptedException {
        return this.submit().get();
    }
}
