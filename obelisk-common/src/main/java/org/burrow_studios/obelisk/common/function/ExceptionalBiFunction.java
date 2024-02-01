package org.burrow_studios.obelisk.common.function;

@FunctionalInterface
public interface ExceptionalBiFunction<P1, P2, R, T extends Throwable> {
    R apply(P1 p1, P2 p2) throws T;
}
