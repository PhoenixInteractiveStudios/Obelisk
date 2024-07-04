package org.burrow_studios.obelisk.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;

public final class CloseableLock implements AutoCloseable {
    private final Lock lock;

    public CloseableLock(@NotNull Lock lock) {
        this.lock = lock;
        this.lock.lock();
    }

    @Override
    public void close() {
        this.lock.unlock();
    }
}
