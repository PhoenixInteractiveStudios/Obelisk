package org.burrow_studios.obelkisk.core.form;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CheckQuery extends QueryElement<Boolean> {
    public static final String IDENTIFIER = "check";

    public CheckQuery(@NotNull String id, @NotNull String title, @Nullable String description, boolean optional) {
        super(id, title, description, optional, false);
    }

    public CheckQuery(@NotNull String id, @NotNull String title, @Nullable String description, boolean optional, boolean done) {
        super(id, title, description, optional, false, done, done);
    }
}
