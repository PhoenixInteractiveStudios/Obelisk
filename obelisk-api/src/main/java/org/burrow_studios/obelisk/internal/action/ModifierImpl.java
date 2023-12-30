package org.burrow_studios.obelisk.internal.action;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.Obelisk;
import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModifierImpl<T extends Turtle> implements Modifier<T> {
    protected final T entity;
    private final @NotNull JsonObject json;

    public ModifierImpl(@NotNull T entity) {
        this.entity = entity;
        this.json = new JsonObject();
        this.json.addProperty("id", entity.getId());
    }

    @Override
    public final @NotNull Obelisk getAPI() {
        return this.entity.getAPI();
    }

    @Override
    public final @NotNull T getEntity() {
        return this.entity;
    }

    /* - JSON - */

    public final @NotNull JsonObject toJson() {
        return this.json.deepCopy();
    }

    protected final void set(@NotNull String path, @NotNull JsonElement json) {
        this.json.add(path, json);
    }

    protected final void add(@NotNull String path, @NotNull JsonArray json) {
        JsonArray arr = this.json.getAsJsonArray(path);
        if (arr == null)
            arr = new JsonArray();
        for (JsonElement element : json)
            arr.add(element);
        this.json.add(path, arr);
    }

    protected final void remove(@NotNull String path, @NotNull JsonArray json) {
        JsonArray arr = this.json.getAsJsonArray(path);
        if (arr == null)
            arr = new JsonArray();
        for (JsonElement element : json)
            arr.remove(element);
        this.json.add(path, arr);
    }

    /* - NETWORKING - */

    @Override
    public final void queue() {
        // TODO
    }

    @Override
    public final @NotNull CompletableFuture<T> submit() {
        // TODO
        return null;
    }
}
