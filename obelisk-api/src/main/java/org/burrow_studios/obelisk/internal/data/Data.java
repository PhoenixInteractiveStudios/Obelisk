package org.burrow_studios.obelisk.internal.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;

public abstract class Data<T extends Turtle> {
    private final @NotNull JsonObject json;

    protected Data() {
        this.json = new JsonObject();
    }

    protected Data(long id) {
        this.json = new JsonObject();
        this.json.addProperty("id", id);
    }

    public final @NotNull JsonObject toJson() {
        return this.json.deepCopy();
    }

    /* - - - */

    protected final void set(@NotNull String path, @NotNull JsonElement json) {
        this.json.add(path, json);
    }

    protected final void addToArray(@NotNull String path, @NotNull JsonArray elements) {
        final JsonElement currentElement = this.json.get("path");
        final JsonArray array = currentElement != null
                ? currentElement.getAsJsonArray()
                : new JsonArray();

        array.addAll(elements);

        this.json.add(path, array);
    }

    protected final void removeFromArray(@NotNull String path, @NotNull JsonArray elements) {
        final JsonElement currentElement = this.json.get("path");

        if (currentElement == null) return;

        final JsonArray array = currentElement.getAsJsonArray();

        for (JsonElement element : elements)
            array.remove(element);

        this.json.add(path, array);
    }
}
