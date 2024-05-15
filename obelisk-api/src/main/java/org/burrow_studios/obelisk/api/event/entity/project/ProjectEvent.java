package org.burrow_studios.obelisk.api.event.entity.project;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.event.entity.AbstractEntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class ProjectEvent extends AbstractEntityEvent<Project> {
    protected ProjectEvent(long id, @NotNull Project entity) {
        super(id, entity);
    }
}
