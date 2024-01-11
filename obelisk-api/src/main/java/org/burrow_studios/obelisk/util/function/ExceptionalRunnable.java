package org.burrow_studios.obelisk.util.function;

@FunctionalInterface
public interface ExceptionalRunnable<T extends Throwable> {
    void run() throws T;
}
