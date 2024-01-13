package org.burrow_studios.obelisk.api.event.entity.group;

import org.burrow_studios.obelisk.api.entities.Group;
import org.jetbrains.annotations.NotNull;

public final class GroupUpdateNameEvent extends GroupUpdateEvent<String> {
    public GroupUpdateNameEvent(long id, @NotNull Group entity, @NotNull String oldValue, @NotNull String newValue) {
        super(id, entity, oldValue, newValue);
    }
}
