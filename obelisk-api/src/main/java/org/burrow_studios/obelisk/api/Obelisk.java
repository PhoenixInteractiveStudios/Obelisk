package org.burrow_studios.obelisk.api;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface Obelisk {
    void awaitReady() throws InterruptedException;

    void awaitReady(long timeout, @NotNull TimeUnit unit) throws InterruptedException, TimeoutException;

    void awaitShutdown() throws InterruptedException;

    void awaitShutdown(long timeout, @NotNull TimeUnit unit) throws InterruptedException, TimeoutException;
}
