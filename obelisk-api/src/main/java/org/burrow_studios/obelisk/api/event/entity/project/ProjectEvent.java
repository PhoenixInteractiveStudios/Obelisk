package org.burrow_studios.obelisk.api.event.entity.project;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract sealed class ProjectEvent extends EntityEvent<Project> permits ProjectCreateEvent, ProjectDeleteEvent, ProjectUpdateEvent {
    protected ProjectEvent(long id, @NotNull Project entity) {
        super(id, entity);
    }
}
