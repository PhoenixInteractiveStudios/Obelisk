package org.burrow_studios.obelisk.core.entities.action.project;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.project.ProjectBuilder;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.BuilderImpl;
import org.burrow_studios.obelisk.core.entities.checks.ProjectChecks;
import org.burrow_studios.obelisk.core.entities.impl.ProjectImpl;
import org.burrow_studios.obelisk.commons.http.Endpoints;
import org.jetbrains.annotations.NotNull;

public class ProjectBuilderImpl extends BuilderImpl<Project> implements ProjectBuilder {
    public ProjectBuilderImpl(@NotNull ObeliskImpl api) {
        super(
                api,
                Project.class,
                Endpoints.Project.CREATE.builder().compile(),
                ProjectImpl::new
        );
    }

    @Override
    public @NotNull ProjectBuilderImpl setTitle(@NotNull String title) throws IllegalArgumentException {
        ProjectChecks.checkTitle(title);
        data.set("title", new JsonPrimitive(title));
        return this;
    }

    @Override
    public @NotNull ProjectBuilderImpl setTimings(@NotNull Project.Timings timings) {
        data.set("timings", ProjectUtils.serializeProjectTimings(timings));
        return this;
    }

    @Override
    public @NotNull ProjectBuilderImpl setState(@NotNull Project.State state) {
        data.set("state", new JsonPrimitive(state.name()));
        return this;
    }

    @Override
    public @NotNull ProjectBuilderImpl addMembers(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        data.addToArray("members", arr);
        return this;
    }
}
