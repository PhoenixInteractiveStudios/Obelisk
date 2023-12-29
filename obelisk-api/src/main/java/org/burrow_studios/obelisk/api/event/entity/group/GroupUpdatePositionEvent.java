package org.burrow_studios.obelisk.api.event.entity.group;

import org.burrow_studios.obelisk.api.entities.Group;
import org.jetbrains.annotations.NotNull;

public final class GroupUpdatePositionEvent extends GroupUpdateEvent<Integer> {
    public GroupUpdatePositionEvent(@NotNull Group entity, @NotNull Integer oldValue, @NotNull Integer newValue) {
        super(entity, oldValue, newValue);
    }
}
