package org.burrow_studios.obelisk.api.event.entity.project;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class ProjectEvent extends EntityEvent<Project> {
    protected ProjectEvent(@NotNull Project entity) {
        super(entity);
    }
}
