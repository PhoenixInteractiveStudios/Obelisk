package org.burrow_studios.obelisk.internal.action;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.Builder;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.common.function.ExceptionalBiFunction;
import org.burrow_studios.obelisk.internal.EntityBuilder;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.net.http.CompiledRoute;
import org.jetbrains.annotations.NotNull;

public abstract class BuilderImpl<T extends Turtle> extends ActionImpl<T> implements Builder<T> {
    private final @NotNull Class<T> type;
    private final @NotNull JsonObject json;

    public BuilderImpl(
            @NotNull ObeliskImpl api,
            @NotNull Class<T> type,
            @NotNull CompiledRoute route,
            @NotNull ExceptionalBiFunction<EntityBuilder, JsonObject, T, ? extends Exception> builder
    ) {
        super(api, route, (request, response) -> {
            // TODO: checks (error responses)
            final T entity = builder.apply(api.getEntityBuilder(), response.getContent().getAsJsonObject());
            // TODO: fire events
            return entity;
        });
        this.type = type;
        this.json = new JsonObject();
    }

    @Override
    public final @NotNull JsonElement getContent() {
        return json.deepCopy();
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
