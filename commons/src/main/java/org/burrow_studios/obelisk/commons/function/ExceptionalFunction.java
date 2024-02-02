package org.burrow_studios.obelisk.commons.function;

@FunctionalInterface
public interface ExceptionalFunction<P, R, T extends Throwable> {
    R apply(P p) throws T;
}
