package org.burrow_studios.obelisk.internal.action.entity.project;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.project.ProjectBuilder;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.internal.EntityBuilder;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.action.BuilderImpl;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class ProjectBuilderImpl extends BuilderImpl<Project> implements ProjectBuilder {
    public ProjectBuilderImpl(@NotNull ObeliskImpl api) {
        super(
                api,
                Project.class,
                Route.Project.CREATE.builder().compile(),
                EntityBuilder::buildProject
        );
    }

    @Override
    public @NotNull ProjectBuilderImpl setTitle(@NotNull String title) {
        this.set("title", new JsonPrimitive(title));
        return this;
    }

    @Override
    public @NotNull ProjectBuilderImpl setTimings(@NotNull Project.Timings timings) {
        JsonObject json = new JsonObject();
        if (timings.release() != null)
            json.addProperty("release", timings.release().toString());
        if (timings.apply() != null)
            json.addProperty("apply", timings.apply().toString());
        if (timings.start() != null)
            json.addProperty("start", timings.start().toString());
        if (timings.end() != null)
            json.addProperty("end", timings.end().toString());
        this.set("timings", json);
        return this;
    }

    @Override
    public @NotNull ProjectBuilderImpl setState(@NotNull Project.State state) {
        this.set("state", new JsonPrimitive(state.name()));
        return this;
    }

    @Override
    public @NotNull ProjectBuilderImpl addMembers(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.add("members", arr);
        return this;
    }}
