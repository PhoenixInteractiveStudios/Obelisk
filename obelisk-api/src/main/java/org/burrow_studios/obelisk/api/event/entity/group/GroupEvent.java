package org.burrow_studios.obelisk.api.event.entity.group;

import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class GroupEvent extends EntityEvent<Group> {
    protected GroupEvent(long id, @NotNull Group entity) {
        super(id, entity);
    }
}
