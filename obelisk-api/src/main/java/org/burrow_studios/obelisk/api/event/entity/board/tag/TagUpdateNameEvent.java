package org.burrow_studios.obelisk.api.event.entity.board.tag;

import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.jetbrains.annotations.NotNull;

public final class TagUpdateNameEvent extends TagUpdateEvent<String> {
    public TagUpdateNameEvent(long id, @NotNull Tag entity, @NotNull String oldValue, @NotNull String newValue) {
        super(id, entity, oldValue, newValue);
    }

    @Override
    public int getOpcode() {
        return 162;
    }
}
