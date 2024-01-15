package org.burrow_studios.obelisk.api.event.entity.board.tag;

import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public final class TagDeleteEvent extends TagEvent implements EntityDeleteEvent<Tag> {
    public TagDeleteEvent(long id, @NotNull Tag entity) {
        super(id, entity);
    }
}
