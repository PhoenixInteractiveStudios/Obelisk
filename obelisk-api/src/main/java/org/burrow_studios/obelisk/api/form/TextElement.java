package org.burrow_studios.obelisk.api.form;

import org.jetbrains.annotations.NotNull;

public class TextElement extends FormElement {
    public static final String IDENTIFIER = "text";

    public TextElement(@NotNull String id, @NotNull String title, @NotNull String content) {
        super(id, title, content);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public @NotNull String getDescription() {
        return super.getDescription();
    }
}
