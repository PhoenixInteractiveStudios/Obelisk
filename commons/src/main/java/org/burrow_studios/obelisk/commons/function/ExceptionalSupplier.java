package org.burrow_studios.obelisk.commons.function;

@FunctionalInterface
public interface ExceptionalSupplier<T, E extends Throwable> {
    T get() throws E;
}
