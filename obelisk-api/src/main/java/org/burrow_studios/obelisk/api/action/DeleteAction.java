package org.burrow_studios.obelisk.api.action;

import org.burrow_studios.obelisk.api.Obelisk;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface DeleteAction<T extends Turtle> {
    @NotNull Obelisk getAPI();

    long getId();

    @NotNull Class<T> getType();

    /** Submits this action to be completed asynchronously. */
    void queue();

    /** Submits this action and returns a {@link CompletableFuture} that will be completed  */
    @NotNull CompletableFuture<Void> submit();

    default void await(long timeout, @NotNull TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        this.submit().get(timeout, unit);
    }

    default void await() throws ExecutionException, InterruptedException {
        this.submit().get();
    }
}
