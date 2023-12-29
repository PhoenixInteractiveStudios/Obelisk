package org.burrow_studios.obelisk.api.event.entity.project;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public final class ProjectDeleteEvent extends ProjectEvent implements EntityDeleteEvent<Project> {
    public ProjectDeleteEvent(@NotNull Project entity) {
        super(entity);
    }
}
