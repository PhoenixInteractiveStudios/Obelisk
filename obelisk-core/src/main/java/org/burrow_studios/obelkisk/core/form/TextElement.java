package org.burrow_studios.obelkisk.core.form;

import org.jetbrains.annotations.NotNull;

public class TextElement extends FormElement {
    public static final String IDENTIFIER = "text";

    private final String content;

    public TextElement(@NotNull String id, @NotNull String title, @NotNull String content) {
        super(id, title);
        this.content = content;
    }

    public @NotNull String getContent() {
        return this.content;
    }
}
