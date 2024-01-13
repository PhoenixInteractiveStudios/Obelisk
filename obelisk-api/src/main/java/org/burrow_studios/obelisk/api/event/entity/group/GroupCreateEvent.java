package org.burrow_studios.obelisk.api.event.entity.group;

import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public final class GroupCreateEvent extends GroupEvent implements EntityDeleteEvent<Group> {
    public GroupCreateEvent(long id, @NotNull Group entity) {
        super(id, entity);
    }
}
