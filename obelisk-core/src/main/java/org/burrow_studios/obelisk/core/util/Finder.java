package org.burrow_studios.obelisk.core.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public final class Finder<T> {
    private T value;

    private Finder(T val) {
        this.value = val;
    }

    public static <T> @NotNull Finder<T> empty(@NotNull Class<T> type) {
        return new Finder<>(null);
    }

    public Finder<T> orElseFind(@NotNull Supplier<T> supplier) {
        if (this.value == null)
            this.value = supplier.get();
        return this;
    }

    public @Nullable T get() {
        return this.value;
    }
}
