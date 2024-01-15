package org.burrow_studios.obelisk.api.event.entity.group;

import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.event.entity.AbstractEntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract sealed class GroupEvent extends AbstractEntityEvent<Group> permits GroupCreateEvent, GroupDeleteEvent, GroupUpdateEvent {
    protected GroupEvent(long id, @NotNull Group entity) {
        super(id, entity);
    }
}
