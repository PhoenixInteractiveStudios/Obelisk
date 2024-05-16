package org.burrow_studios.obelisk.client.action.entity.project;

import org.burrow_studios.obelisk.api.action.entity.project.ProjectBuilder;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.client.EntityBuilder;
import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.client.action.BuilderImpl;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;

public class ProjectBuilderImpl extends BuilderImpl<Project> implements ProjectBuilder {
    public ProjectBuilderImpl(@NotNull ObeliskImpl obelisk) {
        super(obelisk, createRoute(), EntityBuilder::buildProject);
    }

    private static Route.Compiled createRoute() {
        return Route.Project.CREATE_PROJECT.compile();
    }

    @Override
    public @NotNull ProjectBuilderImpl setTitle(@NotNull String title) {
        this.data.addProperty("title", title);
        return this;
    }

    @Override
    public @NotNull ProjectBuilderImpl setState(@NotNull Project.State state) {
        this.data.addProperty("state", state.name());
        return this;
    }
}
