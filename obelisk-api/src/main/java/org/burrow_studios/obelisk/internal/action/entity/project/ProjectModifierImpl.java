package org.burrow_studios.obelisk.internal.action.entity.project;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.project.ProjectModifier;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.internal.EntityUpdater;
import org.burrow_studios.obelisk.internal.action.ModifierImpl;
import org.burrow_studios.obelisk.internal.entities.ProjectImpl;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class ProjectModifierImpl extends ModifierImpl<Project> implements ProjectModifier {
    public ProjectModifierImpl(@NotNull ProjectImpl project) {
        super(
                project,
                Route.Project.EDIT.builder()
                        .withArg(project.getId())
                        .compile(),
                json -> EntityUpdater.updateProject(project, json)
        );
    }

    @Override
    public @NotNull ProjectModifierImpl setTitle(@NotNull String title) {
        this.set("title", new JsonPrimitive(title));
        return this;
    }

    @Override
    public @NotNull ProjectModifierImpl setTimings(@NotNull Project.Timings timings) {
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
    public @NotNull ProjectModifierImpl setState(@NotNull Project.State state) {
        this.set("state", new JsonPrimitive(state.name()));
        return this;
    }

    @Override
    public @NotNull ProjectModifierImpl addMembers(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.add("members", arr);
        return this;
    }

    @Override
    public @NotNull ProjectModifierImpl removeMembers(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.remove("members", arr);
        return this;
    }
}
