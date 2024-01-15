package org.burrow_studios.obelisk.api.event.entity.project;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.event.GatewayOpcodes;
import org.jetbrains.annotations.NotNull;

public final class ProjectUpdateStateEvent extends ProjectUpdateEvent<Project.State> {
    public ProjectUpdateStateEvent(long id, @NotNull Project entity, @NotNull Project.State oldValue, @NotNull Project.State newValue) {
        super(id, entity, oldValue, newValue);
    }

    @Override
    public int getOpcode() {
        return GatewayOpcodes.PROJECT_UPDATE_STATE_EVENT;
    }
}
