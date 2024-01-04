package org.burrow_studios.obelisk.common.function;

@FunctionalInterface
public interface ExceptionalConsumer<T, E extends Throwable> {
    void accept(T t) throws E;
}
