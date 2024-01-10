package org.burrow_studios.obelisk.util.function;

@FunctionalInterface
public interface ExceptionalFunction<P, R, T extends Throwable> {
    R apply(P p) throws T;
}
