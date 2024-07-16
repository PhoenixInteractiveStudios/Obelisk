package org.burrow_studios.obelisk.api.form;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextElement extends FormElement {
    public static final String IDENTIFIER = "text";

    public TextElement(@NotNull String id, @Nullable String title, @NotNull String content) {
        super(id, title, content);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public @NotNull String getDescription() {
        return super.getDescription();
    }
}
