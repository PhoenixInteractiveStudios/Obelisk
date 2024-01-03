package org.burrow_studios.obelisk.api.event.entity.project;

import org.burrow_studios.obelisk.api.entities.Project;
import org.jetbrains.annotations.NotNull;

public final class ProjectUpdateTimingsEvent extends ProjectUpdateEvent<Project.Timings> {
    public ProjectUpdateTimingsEvent(@NotNull Project entity, @NotNull Project.Timings oldValue, @NotNull Project.Timings newValue) {
        super(entity, oldValue, newValue);
    }
}
