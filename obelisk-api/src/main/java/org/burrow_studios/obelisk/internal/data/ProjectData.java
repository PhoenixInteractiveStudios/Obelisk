package org.burrow_studios.obelisk.internal.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.internal.EntityBuilder;
import org.burrow_studios.obelisk.internal.entities.ProjectImpl;
import org.jetbrains.annotations.NotNull;

public final class ProjectData extends Data<ProjectImpl> {
    public ProjectData() {
        super();
    }

    public ProjectData(long id) {
        super(id);
    }

    public ProjectData(@NotNull JsonObject json) {
        super(json);
    }

    @Override
    public @NotNull ProjectImpl build(@NotNull EntityBuilder builder) {
        return builder.buildProject(toJson());
    }

    @Override
    public void update(@NotNull ProjectImpl project) {
        final JsonObject json = toJson();

        handleUpdate(json, "title", JsonElement::getAsString, project::setTitle);
        handleUpdateObject(json, "timings", project.getAPI(), EntityBuilder::buildProjectTimings, project::setTimings);
        handleUpdateEnum(json, "state", Project.State.class, project::setState);
        handleUpdateTurtles(json, "members", project::getMembers);
    }

    public void setTitle(@NotNull String title) {
        this.set("title", new JsonPrimitive(title));
    }

    public void setTimings(@NotNull Project.Timings timings) {
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
    }

    public void setState(@NotNull Project.State state) {
        this.set("state", new JsonPrimitive(state.name()));
    }

    public void addMembers(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.addToArray("members", arr);
    }

    public void removeMembers(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.removeFromArray("members", arr);
    }
}
