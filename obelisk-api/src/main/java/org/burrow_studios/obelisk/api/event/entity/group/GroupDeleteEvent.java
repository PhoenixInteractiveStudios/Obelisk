package org.burrow_studios.obelisk.api.event.entity.group;

import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.event.GatewayOpcodes;
import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public final class GroupDeleteEvent extends GroupEvent implements EntityDeleteEvent<Group> {
    public GroupDeleteEvent(long id, @NotNull Group entity) {
        super(id, entity);
    }

    @Override
    public int getOpcode() {
        return GatewayOpcodes.GROUP_DELETE_EVENT;
    }
}
