package org.burrow_studios.obelisk.client.action.entity.project;

import org.burrow_studios.obelisk.api.action.entity.project.ProjectModifier;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.client.action.ModifierImpl;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;

public class ProjectModifierImpl extends ModifierImpl<Project> implements ProjectModifier {
    public ProjectModifierImpl(@NotNull Project entity) {
        super(entity, createRoute(entity));
    }

    private static Route.Compiled createRoute(@NotNull Project entity) {
        return Route.Project.EDIT_PROJECT.compile(entity.getId());
    }

    @Override
    public @NotNull ProjectModifierImpl setTitle(@NotNull String title) {
        this.data.addProperty("title", title);
        return this;
    }

    @Override
    public @NotNull ProjectModifierImpl setState(@NotNull Project.State state) {
        this.data.addProperty("state", state.name());
        return this;
    }
}
