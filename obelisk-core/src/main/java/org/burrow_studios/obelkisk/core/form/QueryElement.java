package org.burrow_studios.obelkisk.core.form;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public abstract class QueryElement<T> extends FormElement {
    private final AtomicReference<T> value = new AtomicReference<>();
    private final AtomicBoolean      done  = new AtomicBoolean();

    private final boolean optional;
    private final T defaultValue;

    protected QueryElement(@NotNull String id, @NotNull String title, boolean optional, T defaultValue) {
        this(id, title, optional, defaultValue, null, false);
    }

    protected QueryElement(@NotNull String id, @NotNull String title, boolean optional, T defaultValue, T value, boolean done) {
        super(id, title);
        this.value.set(value);
        this.done.set(done);

        this.optional = optional;
        this.defaultValue = defaultValue;
    }

    public final void setDone() {
        T val = this.getValue();

        if (val == null && !this.isOptional())
            throw new IllegalStateException("Value must not be null");

        this.checks(val);

        this.done.set(true);
    }

    protected void checks(T value) throws IllegalArgumentException { }

    public final boolean isDone() {
        return this.done.get();
    }

    public void clear() {
        this.value.set(null);
        this.done.set(false);
    }

    public final boolean isOptional() {
        return this.optional;
    }

    public final T getValue() {
        if (this.value.get() == null)
            return this.defaultValue;
        return this.value.get();
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public void setValue(T val) {
        this.value.set(val);
    }
}
