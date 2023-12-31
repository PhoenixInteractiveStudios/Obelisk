package org.burrow_studios.obelisk.internal.action.entity.project;

import org.burrow_studios.obelisk.api.action.entity.project.ProjectBuilder;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.internal.EntityBuilder;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.action.BuilderImpl;
import org.burrow_studios.obelisk.internal.data.ProjectData;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class ProjectBuilderImpl extends BuilderImpl<Project, ProjectData> implements ProjectBuilder {
    public ProjectBuilderImpl(@NotNull ObeliskImpl api) {
        super(
                api,
                Project.class,
                Route.Project.CREATE.builder().compile(),
                new ProjectData(),
                EntityBuilder::buildProject
        );
    }

    @Override
    public @NotNull ProjectBuilderImpl setTitle(@NotNull String title) {
        this.data.setTitle(title);
        return this;
    }

    @Override
    public @NotNull ProjectBuilderImpl setTimings(@NotNull Project.Timings timings) {
        this.data.setTimings(timings);
        return this;
    }

    @Override
    public @NotNull ProjectBuilderImpl setState(@NotNull Project.State state) {
        this.data.setState(state);
        return this;
    }

    @Override
    public @NotNull ProjectBuilderImpl addMembers(@NotNull User... users) {
        this.data.addMembers(users);
        return this;
    }}
