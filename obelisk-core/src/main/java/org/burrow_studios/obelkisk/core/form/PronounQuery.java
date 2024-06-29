package org.burrow_studios.obelkisk.core.form;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PronounQuery extends QueryElement<String> {
    public static final String IDENTIFIER = "pronoun";

    private final List<String> suggestions;

    public PronounQuery(@NotNull String id, @NotNull String title, boolean optional, String defaultValue) {
        this(id, title, optional, defaultValue, List.of());
    }

    public PronounQuery(@NotNull String id, @NotNull String title, boolean optional, String defaultValue, @NotNull List<String> suggestions) {
        super(id, title, optional, defaultValue);
        this.suggestions = List.copyOf(suggestions);
    }

    public PronounQuery(@NotNull String id, @NotNull String title, boolean optional, String defaultValue, @NotNull List<String> suggestions, String selected, boolean done) {
        super(id, title, optional, defaultValue, selected, done);
        this.suggestions = List.copyOf(suggestions);
    }

    /* - - - - - */

    public @NotNull List<String> getSuggestions() {
        return this.suggestions;
    }
}
