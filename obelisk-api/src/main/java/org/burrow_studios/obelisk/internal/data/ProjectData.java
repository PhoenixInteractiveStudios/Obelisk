package org.burrow_studios.obelisk.internal.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.cache.DelegatingTurtleCacheView;
import org.burrow_studios.obelisk.internal.entities.ProjectImpl;
import org.burrow_studios.obelisk.internal.entities.UserImpl;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

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
    public @NotNull ProjectImpl build(@NotNull ObeliskImpl api) {
        final JsonObject json = toJson();

        final long   id       = json.get("id").getAsLong();
        final String title    = json.get("title").getAsString();
        final String stateStr = json.get("state").getAsString();

        final Project.Timings timings = buildProjectTimings(json.getAsJsonObject("timings"));

        final Project.State state = Project.State.valueOf(stateStr);

        final DelegatingTurtleCacheView<UserImpl> members = buildDelegatingCacheView(json, "members", api.getUsers(), UserImpl.class);

        final ProjectImpl project = new ProjectImpl(api, id, title, timings, state, members);

        api.getProjects().add(project);
        return project;
    }

    public static @NotNull Project.Timings buildProjectTimings(@NotNull JsonObject json) {
        final String releaseStr = json.get("release").getAsString();
        final String   applyStr = json.get("apply").getAsString();
        final String   startStr = json.get("start").getAsString();
        final String     endStr = json.get("end").getAsString();

        final Instant release = Instant.parse(releaseStr);
        final Instant apply   = Instant.parse(applyStr);
        final Instant start   = Instant.parse(startStr);
        final Instant end     = Instant.parse(endStr);

        return new Project.Timings(release, apply, start, end);
    }

    @Override
    public void update(@NotNull ProjectImpl project) {
        final JsonObject json = toJson();

        handleUpdate(json, "title", JsonElement::getAsString, project::setTitle);
        handleUpdateObject(json, "timings", project.getAPI(), ProjectData::buildProjectTimings, project::setTimings);
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
}
