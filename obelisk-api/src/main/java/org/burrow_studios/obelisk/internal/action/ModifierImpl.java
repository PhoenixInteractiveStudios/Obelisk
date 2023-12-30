package org.burrow_studios.obelisk.internal.action;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.net.http.CompiledRoute;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public abstract class ModifierImpl<T extends Turtle> extends ActionImpl<T> implements Modifier<T> {
    protected final T entity;
    private final @NotNull JsonObject json;

    public ModifierImpl(@NotNull T entity, @NotNull CompiledRoute route, @NotNull Consumer<JsonObject> updater) {
        super(((ObeliskImpl) entity.getAPI()), route, (request, response) -> {
            // TODO: handle errors
            JsonObject content = response.getContent().getAsJsonObject();
            updater.accept(content);
            return entity;
        });
        this.entity = entity;
        this.json = new JsonObject();
        this.json.addProperty("id", entity.getId());
    }

    @Override
    public final @NotNull T getEntity() {
        return this.entity;
    }

    @Override
    public final @NotNull JsonElement getContent() {
        return this.json.deepCopy();
    }

    /* - JSON - */

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
}
