package org.burrow_studios.obelisk.util.function;

@FunctionalInterface
public interface ExceptionalSupplier<T, E extends Throwable> {
    T get() throws E;
}
