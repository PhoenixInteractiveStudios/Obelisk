package org.burrow_studios.obelisk.api.form;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PronounQuery extends QueryElement<String> {
    public static final String IDENTIFIER = "pronoun";

    private final List<String> suggestions;

    public PronounQuery(@NotNull String id, @NotNull String title, @Nullable String description, boolean optional, String defaultValue) {
        this(id, title, description, optional, defaultValue, List.of());
    }

    public PronounQuery(@NotNull String id, @NotNull String title, @Nullable String description, boolean optional, String defaultValue, @NotNull List<String> suggestions) {
        super(id, title, description, optional, defaultValue);
        this.suggestions = List.copyOf(suggestions);
    }

    public PronounQuery(@NotNull String id, @NotNull String title, @Nullable String description, boolean optional, String defaultValue, @NotNull List<String> suggestions, String selected, boolean done) {
        super(id, title, description, optional, defaultValue, selected, done);
        this.suggestions = List.copyOf(suggestions);
    }

    /* - - - - - */

    public @NotNull List<String> getSuggestions() {
        return this.suggestions;
    }
}
