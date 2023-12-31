package org.burrow_studios.obelisk.internal.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

public final class ProjectData extends Data<Project> {
    public ProjectData() {
        super();
    }

    public ProjectData(long id) {
        super(id);
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
