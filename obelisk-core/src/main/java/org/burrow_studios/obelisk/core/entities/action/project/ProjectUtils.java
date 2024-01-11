package org.burrow_studios.obelisk.core.entities.action.project;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.Project;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

class ProjectUtils {
    private ProjectUtils() { }

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

    public static @NotNull JsonObject serializeProjectTimings(@NotNull Project.Timings timings) {
        JsonObject json = new JsonObject();
        if (timings.release() != null)
            json.addProperty("release", timings.release().toString());
        if (timings.apply() != null)
            json.addProperty("apply", timings.apply().toString());
        if (timings.start() != null)
            json.addProperty("start", timings.start().toString());
        if (timings.end() != null)
            json.addProperty("end", timings.end().toString());
        return json;
    }
}
