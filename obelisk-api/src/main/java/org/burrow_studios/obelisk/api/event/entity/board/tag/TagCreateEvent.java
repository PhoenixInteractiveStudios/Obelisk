package org.burrow_studios.obelisk.api.event.entity.board.tag;

import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.api.event.entity.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public final class TagCreateEvent extends TagEvent implements EntityCreateEvent<Tag> {
    public TagCreateEvent(long id, @NotNull Tag entity) {
        super(id, entity);
    }

    @Override
    public int getOpcode() {
        return 160;
    }
}
