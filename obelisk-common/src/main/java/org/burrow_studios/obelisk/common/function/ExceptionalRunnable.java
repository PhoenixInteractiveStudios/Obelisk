package org.burrow_studios.obelisk.common.function;

@FunctionalInterface
public interface ExceptionalRunnable<T extends Throwable> {
    void run() throws T;
}
