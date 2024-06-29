package org.burrow_studios.obelkisk.core.form;

import org.jetbrains.annotations.NotNull;

public abstract class QueryElement<T> extends FormElement {
    private T value;
    private boolean done;

    private final boolean optional;
    private final T defaultValue;

    protected QueryElement(@NotNull String id, @NotNull String title, boolean optional, T defaultValue) {
        this(id, title, optional, defaultValue, null, false);
    }

    protected QueryElement(@NotNull String id, @NotNull String title, boolean optional, T defaultValue, T value, boolean done) {
        super(id, title);
        this.value = value;
        this.done = done;

        this.optional = optional;
        this.defaultValue = defaultValue;
    }

    public final void setDone() {
        T val = this.getValue();

        if (val == null && !this.isOptional())
            throw new IllegalStateException("Value must not be null");

        this.checks(val);

        this.done = true;
    }

    protected void checks(T value) throws IllegalArgumentException { }

    public final boolean isDone() {
        return this.done;
    }

    public void clear() {
        this.value = null;
        this.done = false;
    }

    public final boolean isOptional() {
        return this.optional;
    }

    public final T getValue() {
        if (this.value == null)
            return this.defaultValue;
        return this.value;
    }

    public void setValue(T val) {
        this.value = val;
    }
}
