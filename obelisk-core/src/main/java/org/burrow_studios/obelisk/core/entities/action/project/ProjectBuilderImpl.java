package org.burrow_studios.obelisk.core.entities.action.project;

import org.burrow_studios.obelisk.api.action.entity.project.ProjectBuilder;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.BuilderImpl;
import org.burrow_studios.obelisk.core.entities.data.ProjectData;
import org.burrow_studios.obelisk.core.entities.impl.ProjectImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class ProjectBuilderImpl extends BuilderImpl<Project, ProjectImpl, ProjectData> implements ProjectBuilder {
    public ProjectBuilderImpl(@NotNull ObeliskImpl api) {
        super(
                api,
                Project.class,
                Route.Project.CREATE.builder().compile(),
                new ProjectData(),
                ProjectData::new
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
