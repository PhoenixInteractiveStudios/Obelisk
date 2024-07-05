package org.burrow_studios.obelisk.api.form;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FormElement {
    private final String id;
    private final String title;
    private final String description;

    protected FormElement(@NotNull String id, @NotNull String title, @Nullable String description) {
        if (id.isBlank())
            throw new IllegalArgumentException("id must not be blank");

        this.id = id;
        this.title = title;
        this.description = description;
    }

    public final @NotNull String getId() {
        return this.id;
    }

    public @NotNull String getTitle() {
        return this.title;
    }

    public @Nullable String getDescription() {
        return this.description;
    }
}
