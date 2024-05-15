package org.burrow_studios.obelisk.api.action.entity.project;

import org.burrow_studios.obelisk.api.action.Builder;
import org.burrow_studios.obelisk.api.entities.Project;
import org.jetbrains.annotations.NotNull;

public interface ProjectBuilder extends Builder<Project> {
    @NotNull ProjectBuilder setTitle(@NotNull String title);

    @NotNull ProjectBuilder setState(@NotNull Project.State state);
}
