package org.burrow_studios.obelisk.api.event.entity.project;

import org.burrow_studios.obelisk.api.entities.Project;
import org.jetbrains.annotations.NotNull;

public final class ProjectUpdateTitleEvent extends ProjectUpdateEvent<String> {
    public ProjectUpdateTitleEvent(@NotNull Project entity, @NotNull String oldValue, @NotNull String newValue) {
        super(entity, oldValue, newValue);
    }
}
