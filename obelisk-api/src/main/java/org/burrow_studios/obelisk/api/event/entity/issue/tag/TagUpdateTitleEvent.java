package org.burrow_studios.obelisk.api.event.entity.issue.tag;

import org.burrow_studios.obelisk.api.entities.issue.Tag;
import org.jetbrains.annotations.NotNull;

public final class TagUpdateTitleEvent extends TagUpdateEvent<String> {
    public TagUpdateTitleEvent(@NotNull Tag entity, @NotNull String oldValue, @NotNull String newValue) {
        super(entity, oldValue, newValue);
    }
}
