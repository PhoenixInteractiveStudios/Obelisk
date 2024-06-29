package org.burrow_studios.obelkisk.core.form;

import org.jetbrains.annotations.NotNull;

public abstract class FormElement {
    private final String id;
    private final String title;

    protected FormElement(@NotNull String id, @NotNull String title) {
        if (id.isBlank())
            throw new IllegalArgumentException("id must not be blank");

        this.id = id;
        this.title = title;
    }

    public final @NotNull String getId() {
        return this.id;
    }

    public @NotNull String getTitle() {
        return this.title;
    }
}
