package org.burrow_studios.obelisk.api.event.entity.board.tag;

import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.api.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract sealed class TagEvent extends EntityEvent<Tag> permits TagCreateEvent, TagDeleteEvent, TagUpdateEvent {
    protected TagEvent(long id, @NotNull Tag entity) {
        super(id, entity);
    }
}
