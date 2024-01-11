package org.burrow_studios.obelisk.util.function;

@FunctionalInterface
public interface ExceptionalConsumer<T, E extends Throwable> {
    void accept(T t) throws E;
}
