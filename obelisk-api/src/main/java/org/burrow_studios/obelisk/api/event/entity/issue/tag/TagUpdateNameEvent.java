package org.burrow_studios.obelisk.api.event.entity.issue.tag;

import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.jetbrains.annotations.NotNull;

public final class TagUpdateNameEvent extends TagUpdateEvent<String> {
    public TagUpdateNameEvent(@NotNull Tag entity, @NotNull String oldValue, @NotNull String newValue) {
        super(entity, oldValue, newValue);
    }
}
