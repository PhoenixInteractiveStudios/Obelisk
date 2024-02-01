package org.burrow_studios.obelisk.common.function;

@FunctionalInterface
public interface ExceptionalSupplier<T, E extends Throwable> {
    T get() throws E;
}
