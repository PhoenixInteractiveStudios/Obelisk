package org.burrow_studios.obelisk.util;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("DataFlowIssue")
public class Pipe<A, T extends Throwable> {
    private final A obj;
    private final T ex;

    private final Function<String, T> exceptionBuilder;

    private Pipe(A obj, Function<String, T> exceptionBuilder) {
        this.obj = obj;
        this.ex = null;
        this.exceptionBuilder = exceptionBuilder;
    }

    private Pipe(@NotNull T ex) {
        this.obj = null;
        this.ex = ex;
        this.exceptionBuilder = null;
    }

    public static <A, T extends Throwable> @NotNull Pipe<A, T> of(A obj, @NotNull Function<String, T> exceptionBuilder) {
        return new Pipe<>(obj, exceptionBuilder);
    }

    public boolean hasFailed() {
        return ex != null;
    }

    public <B> @NotNull Pipe<B, T> map(@NotNull Function<A, B> map, @NotNull String message) {
        if (hasFailed())
            return new Pipe<>(ex);
        try {
            return new Pipe<>(map.apply(obj), exceptionBuilder);
        } catch (Exception e) {
            return new Pipe<>(exceptionBuilder.apply(message));
        }
    }

    public <B> @NotNull Pipe<B, T> map(@NotNull Function<A, B> map) {
        return this.map(map, "");
    }

    public @NotNull Pipe<A, T> nonNull(@NotNull String message) {
        if (hasFailed())
            return this;
        if (obj != null)
            return this;
        return new Pipe<>(exceptionBuilder.apply(message));
    }

    public A get() throws T {
        if (ex != null)
            throw ex;
        return obj;
    }

    public void ifPresent(@NotNull Consumer<A> handler) throws T {
        if (ex != null)
            throw ex;
        if (this.obj != null)
            handler.accept(this.obj);
    }
}
