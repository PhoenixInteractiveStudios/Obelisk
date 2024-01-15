package org.burrow_studios.obelisk.api.event.entity.group;

import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.event.GatewayOpcodes;
import org.burrow_studios.obelisk.api.event.entity.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public final class GroupCreateEvent extends GroupEvent implements EntityCreateEvent<Group> {
    public GroupCreateEvent(long id, @NotNull Group entity) {
        super(id, entity);
    }

    @Override
    public int getOpcode() {
        return GatewayOpcodes.GROUP_CREATE_EVENT;
    }
}
