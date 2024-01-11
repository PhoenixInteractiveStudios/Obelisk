package org.burrow_studios.obelisk.core.entities.action.project;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.project.ProjectModifier;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.core.action.ModifierImpl;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.checks.ProjectChecks;
import org.burrow_studios.obelisk.core.entities.impl.ProjectImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

import static org.burrow_studios.obelisk.core.entities.UpdateHelper.*;

public class ProjectModifierImpl extends ModifierImpl<Project, ProjectImpl> implements ProjectModifier {
    public ProjectModifierImpl(@NotNull ProjectImpl project) {
        super(
                project,
                Route.Project.EDIT.builder()
                        .withArg(project.getId())
                        .compile(),
                ProjectModifierImpl::update
        );
    }

    protected static void update(@NotNull EntityData data, @NotNull ProjectImpl project) {
        final JsonObject json = data.toJson();

        handleUpdate(json, "title", JsonElement::getAsString, project::setTitle);
        handleUpdateObject(json, "timings", project.getAPI(), ProjectUtils::buildProjectTimings, project::setTimings);
        handleUpdateEnum(json, "state", Project.State.class, project::setState);
        handleUpdateTurtles(json, "members", project::getMembers);
    }

    @Override
    public @NotNull ProjectModifierImpl setTitle(@NotNull String title) throws IllegalArgumentException {
        ProjectChecks.checkTitle(title);
        data.set("title", new JsonPrimitive(title));
        return this;
    }

    @Override
    public @NotNull ProjectModifierImpl setTimings(@NotNull Project.Timings timings) {
        data.set("timings", ProjectUtils.serializeProjectTimings(timings));
        return this;
    }

    @Override
    public @NotNull ProjectModifierImpl setState(@NotNull Project.State state) {
        data.set("state", new JsonPrimitive(state.name()));
        return this;
    }
}
