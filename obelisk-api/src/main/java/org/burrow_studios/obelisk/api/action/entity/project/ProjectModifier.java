package org.burrow_studios.obelisk.api.action.entity.project;

import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

public interface ProjectModifier extends Modifier<Project> {
    @NotNull ProjectModifier setTitle(@NotNull String title);

    @NotNull ProjectModifier setTimings(@NotNull Project.Timings timings);

    @NotNull ProjectModifier setState(@NotNull Project.State state);

    @NotNull ProjectModifier addMembers(@NotNull User... users);

    @NotNull ProjectModifier removeMembers(@NotNull User... users);
}
