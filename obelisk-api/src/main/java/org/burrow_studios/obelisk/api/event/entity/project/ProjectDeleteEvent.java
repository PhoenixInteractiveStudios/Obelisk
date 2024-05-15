package org.burrow_studios.obelisk.api.event.entity.project;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public class ProjectDeleteEvent extends ProjectEvent implements EntityDeleteEvent<Project> {
    public ProjectDeleteEvent(long id, @NotNull Project entity) {
        super(id, entity);
    }
}
