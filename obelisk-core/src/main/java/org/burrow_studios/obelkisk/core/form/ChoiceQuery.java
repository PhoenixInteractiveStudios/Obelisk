package org.burrow_studios.obelkisk.core.form;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChoiceQuery extends QueryElement<String> {
    public static final String IDENTIFIER = "choice";

    private final List<String> options;

    public ChoiceQuery(@NotNull String id, @NotNull String title, @NotNull List<String> options, boolean optional, String defaultValue) {
        super(id, title, optional, defaultValue);
        this.options = List.copyOf(options);
    }

    public ChoiceQuery(@NotNull String id, @NotNull String title, @NotNull List<String> options, boolean optional, String defaultValue, String selected, boolean done) {
        super(id, title, optional, defaultValue, selected, done);
        this.options = List.copyOf(options);
    }

    /* - - - - - */

    public @NotNull List<String> getOptions() {
        return this.options;
    }

    @Override
    protected void checks(String value) throws IllegalArgumentException {
        if (value != null && !options.contains(value))
            throw new IllegalArgumentException("Not an option");
    }
}
