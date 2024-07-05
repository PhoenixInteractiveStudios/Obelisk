package org.burrow_studios.obelisk.api.form;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IntQuery extends QueryElement<Integer> {
    public static final String IDENTIFIER = "int";

    private final int min;
    private final int max;

    public IntQuery(@NotNull String id, @NotNull String title, @Nullable String description, boolean optional, Integer defaultValue) {
        this(id, title, description, Integer.MIN_VALUE, Integer.MAX_VALUE, optional, defaultValue);
    }

    public IntQuery(@NotNull String id, @NotNull String title, @Nullable String description, int min, int max, boolean optional, Integer defaultValue) {
        super(id, title, description, optional, defaultValue);

        if (min > max)
            throw new IllegalArgumentException("min must not be greater than max");

        this.min = min;
        this.max = max;
    }

    public IntQuery(@NotNull String id, @NotNull String title, @Nullable String description, int min, int max, boolean optional, Integer defaultValue, Integer val, boolean done) {
        super(id, title, description, optional, defaultValue, val, done);
        this.min = min;
        this.max = max;
    }

    /* - - - - - */

    public int getMin() {
        return this.min;
    }

    public int getMax() {
        return this.max;
    }

    @Override
    protected void checks(Integer value) throws IllegalArgumentException {
        if (value == null) return;

        if (this.min > value)
            throw new IllegalArgumentException("Minimum value of " + this.min + " required. (is " + value + ")");

        if (this.max < value)
            throw new IllegalArgumentException("Maximum value of " + this.max + " required. (is " + value + ")");
    }
}
