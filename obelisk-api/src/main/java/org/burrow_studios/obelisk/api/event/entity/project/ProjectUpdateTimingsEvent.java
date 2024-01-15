package org.burrow_studios.obelisk.api.event.entity.project;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.event.GatewayOpcodes;
import org.jetbrains.annotations.NotNull;

public final class ProjectUpdateTimingsEvent extends ProjectUpdateEvent<Project.Timings> {
    public ProjectUpdateTimingsEvent(long id, @NotNull Project entity, @NotNull Project.Timings oldValue, @NotNull Project.Timings newValue) {
        super(id, entity, oldValue, newValue);
    }

    @Override
    public int getOpcode() {
        return GatewayOpcodes.PROJECT_UPDATE_TIMINGS_EVENT;
    }
}
