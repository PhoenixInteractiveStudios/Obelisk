package org.burrow_studios.obelisk.api.event.entity.project;

import org.burrow_studios.obelisk.api.entities.Project;
import org.jetbrains.annotations.NotNull;

public final class ProjectUpdateStateEvent extends ProjectUpdateEvent<Project.State> {
    public ProjectUpdateStateEvent(@NotNull Project entity, @NotNull Project.State oldValue, @NotNull Project.State newValue) {
        super(entity, oldValue, newValue);
    }
}
