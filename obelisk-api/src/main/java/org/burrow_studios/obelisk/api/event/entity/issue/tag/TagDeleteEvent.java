package org.burrow_studios.obelisk.api.event.entity.issue.tag;

import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public final class TagDeleteEvent extends TagEvent implements EntityDeleteEvent<Tag> {
    public TagDeleteEvent(@NotNull Tag entity) {
        super(entity);
    }
}
