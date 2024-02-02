package org.burrow_studios.obelisk.commons.function;

@FunctionalInterface
public interface ExceptionalRunnable<T extends Throwable> {
    void run() throws T;
}
