package org.burrow_studios.obelisk.api.event.entity.issue.tag;

import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.api.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class TagEvent extends EntityEvent<Tag> {
    protected TagEvent(@NotNull Tag entity) {
        super(entity);
    }
}
