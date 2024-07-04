package org.burrow_studios.obelisk.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public final class CloseableIdLogRegistry<T> {
    private final Map<T, ReadWriteCloseableLock> locks;

    public CloseableIdLogRegistry() {
        this.locks = Collections.synchronizedMap(new WeakHashMap<>());
    }

    private @NotNull ReadWriteCloseableLock get(@NotNull T id) {
        return this.locks.computeIfAbsent(id, t -> new ReadWriteCloseableLock());
    }

    public @NotNull CloseableLock read(@NotNull T id) {
        return this.get(id).read();
    }

    public @NotNull CloseableLock write(@NotNull T id) {
        return this.get(id).write();
    }
}
