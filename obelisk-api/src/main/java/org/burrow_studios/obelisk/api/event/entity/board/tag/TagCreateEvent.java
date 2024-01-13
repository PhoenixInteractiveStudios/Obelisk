package org.burrow_studios.obelisk.api.event.entity.board.tag;

import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public final class TagCreateEvent extends TagEvent implements EntityDeleteEvent<Tag> {
    public TagCreateEvent(long id, @NotNull Tag entity) {
        super(id, entity);
    }
}
