package org.burrow_studios.obelisk.commons.function;

@FunctionalInterface
public interface ExceptionalConsumer<T, E extends Throwable> {
    void accept(T t) throws E;
}
