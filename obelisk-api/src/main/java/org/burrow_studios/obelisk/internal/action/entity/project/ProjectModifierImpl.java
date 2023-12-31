package org.burrow_studios.obelisk.internal.action.entity.project;

import org.burrow_studios.obelisk.api.action.entity.project.ProjectModifier;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.internal.EntityUpdater;
import org.burrow_studios.obelisk.internal.action.ModifierImpl;
import org.burrow_studios.obelisk.internal.data.ProjectData;
import org.burrow_studios.obelisk.internal.entities.ProjectImpl;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class ProjectModifierImpl extends ModifierImpl<Project, ProjectData> implements ProjectModifier {
    public ProjectModifierImpl(@NotNull ProjectImpl project) {
        super(
                project,
                Route.Project.EDIT.builder()
                        .withArg(project.getId())
                        .compile(),
                new ProjectData(project.getId()),
                json -> EntityUpdater.updateProject(project, json)
        );
    }

    @Override
    public @NotNull ProjectModifierImpl setTitle(@NotNull String title) {
        this.data.setTitle(title);
        return this;
    }

    @Override
    public @NotNull ProjectModifierImpl setTimings(@NotNull Project.Timings timings) {
        this.data.setTimings(timings);
        return this;
    }

    @Override
    public @NotNull ProjectModifierImpl setState(@NotNull Project.State state) {
        this.data.setState(state);
        return this;
    }

    @Override
    public @NotNull ProjectModifierImpl addMembers(@NotNull User... users) {
        this.data.addMembers(users);
        return this;
    }

    @Override
    public @NotNull ProjectModifierImpl removeMembers(@NotNull User... users) {
        this.data.removeMembers(users);
        return this;
    }
}
