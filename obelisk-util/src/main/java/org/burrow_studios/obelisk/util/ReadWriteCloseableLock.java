package org.burrow_studios.obelisk.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class ReadWriteCloseableLock {
    private final ReentrantReadWriteLock lock;

    public ReadWriteCloseableLock() {
        this.lock = new ReentrantReadWriteLock();
    }

    public @NotNull CloseableLock read() {
        return new CloseableLock(this.lock.readLock());
    }

    public @NotNull CloseableLock write() {
        return new CloseableLock(this.lock.writeLock());
    }
}
