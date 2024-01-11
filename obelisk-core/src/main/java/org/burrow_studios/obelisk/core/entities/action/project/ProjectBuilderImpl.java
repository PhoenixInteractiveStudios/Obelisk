package org.burrow_studios.obelisk.core.entities.action.project;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.project.ProjectBuilder;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.BuilderImpl;
import org.burrow_studios.obelisk.core.cache.DelegatingTurtleCacheView;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.impl.ProjectImpl;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

import static org.burrow_studios.obelisk.core.entities.BuildHelper.buildDelegatingCacheView;
import static org.burrow_studios.obelisk.core.entities.action.project.ProjectUtils.buildProjectTimings;

public class ProjectBuilderImpl extends BuilderImpl<Project> implements ProjectBuilder {
    public ProjectBuilderImpl(@NotNull ObeliskImpl api) {
        super(
                api,
                Project.class,
                Route.Project.CREATE.builder().compile(),
                ProjectBuilderImpl::build
        );
    }

    protected static @NotNull ProjectImpl build(@NotNull EntityData data, @NotNull ObeliskImpl api) {
        final JsonObject json = data.toJson();

        final long id = json.get("id").getAsLong();
        final String title = json.get("title").getAsString();
        final String stateStr = json.get("state").getAsString();

        final Project.Timings timings = buildProjectTimings(json.getAsJsonObject("timings"));

        final Project.State state = Project.State.valueOf(stateStr);

        final DelegatingTurtleCacheView<UserImpl> members = buildDelegatingCacheView(json, "members", api.getUsers(), UserImpl.class);

        final ProjectImpl project = new ProjectImpl(api, id, title, timings, state, members);

        api.getProjects().add(project);
        return project;
    }

    @Override
    public @NotNull ProjectBuilderImpl setTitle(@NotNull String title) {
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
