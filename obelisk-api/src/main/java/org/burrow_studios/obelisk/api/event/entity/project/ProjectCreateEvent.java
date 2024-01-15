package org.burrow_studios.obelisk.api.event.entity.project;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.event.entity.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public final class ProjectCreateEvent extends ProjectEvent implements EntityCreateEvent<Project> {
    public ProjectCreateEvent(long id, @NotNull Project entity) {
        super(id, entity);
    }
}
