package org.burrow_studios.obelisk.api.action;

import org.burrow_studios.obelisk.api.Obelisk;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface Action<T> {
    @NotNull Obelisk getAPI();

    /** Submits this action to be completed asynchronously. */
    void queue();

    /** Submits this action and returns a {@link CompletableFuture} that will be completed  */
    @NotNull CompletableFuture<T> submit();

    T await(long timeout, @NotNull TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException;

    T await() throws ExecutionException, InterruptedException;
}
