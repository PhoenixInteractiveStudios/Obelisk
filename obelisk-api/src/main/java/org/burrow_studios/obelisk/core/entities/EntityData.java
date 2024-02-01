package org.burrow_studios.obelisk.core.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;

public class EntityData {
    private final @NotNull JsonObject json;

    public EntityData() {
        this.json = new JsonObject();
    }

    protected EntityData(long id) {
        this.json = new JsonObject();
        this.json.addProperty("id", id);
    }

    public EntityData(@NotNull JsonObject json) {
        this.json = json;
    }

    public final @NotNull JsonObject toJson() {
        return this.json.deepCopy();
    }

    /* - - - */

    public final void set(@NotNull String path, @NotNull JsonElement json) {
        this.json.add(path, json);
    }

    public final void addToArray(@NotNull String path, @NotNull JsonArray elements) {
        final JsonElement currentElement = this.json.get("path");
        final JsonArray array = currentElement != null
                ? currentElement.getAsJsonArray()
                : new JsonArray();

        array.addAll(elements);

        this.json.add(path, array);
    }

    public final void removeFromArray(@NotNull String path, @NotNull JsonArray elements) {
        final JsonElement currentElement = this.json.get("path");

        if (currentElement == null) return;

        final JsonArray array = currentElement.getAsJsonArray();

        for (JsonElement element : elements)
            array.remove(element);

        this.json.add(path, array);
    }

    public final long getId() throws IllegalStateException {
        JsonPrimitive idElement = this.json.getAsJsonPrimitive("id");
        if (idElement == null || idElement.isNumber())
            throw new IllegalStateException("EntityData does not contain a valid id");
        return idElement.getAsLong();
    }
}
