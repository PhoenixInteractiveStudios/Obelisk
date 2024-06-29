package org.burrow_studios.obelkisk.core.form;

import org.jetbrains.annotations.NotNull;

public class TextQuery extends QueryElement<String> {
    public static final String IDENTIFIER = "input";

    private final int minLength;
    private final int maxLength;

    public TextQuery(@NotNull String id, @NotNull String title, boolean optional, String defaultValue) {
        this(id, title, 1, 2000, optional, defaultValue);
    }

    public TextQuery(@NotNull String id, @NotNull String title, int minLength, int maxLength, boolean optional, String defaultValue) {
        super(id, title, optional, defaultValue);

        if (minLength <= 0)
            throw new IllegalArgumentException("minLength must be greater than 0");

        if (minLength > maxLength)
            throw new IllegalArgumentException("minLength must not be greater than maxLength");

        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public TextQuery(@NotNull String id, @NotNull String title, boolean optional, String defaultValue, String input, boolean done) {
        this(id, title, 1, 2000, optional, defaultValue, input, done);
    }

    public TextQuery(@NotNull String id, @NotNull String title, int minLength, int maxLength, boolean optional, String defaultValue, String input, boolean done) {
        super(id, title, optional, defaultValue, input, done);

        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    /* - - - - - */

    public int getMinLength() {
        return this.minLength;
    }

    public int getMaxLength() {
        return this.maxLength;
    }

    @Override
    protected void checks(String value) throws IllegalArgumentException {
        if (value == null) return;

        if (value.length() < this.minLength)
            throw new IllegalArgumentException("Min length of " + this.minLength + " required (is " + value.length() + ")");

        if (value.length() > this.maxLength)
            throw new IllegalArgumentException("Max length of " + this.maxLength + " required (is " + value.length() + ")");
    }
}
