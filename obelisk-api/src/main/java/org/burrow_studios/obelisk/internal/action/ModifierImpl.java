package org.burrow_studios.obelisk.internal.action;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.data.Data;
import org.burrow_studios.obelisk.internal.net.http.CompiledRoute;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public abstract class ModifierImpl<T extends Turtle, D extends Data<T>> extends ActionImpl<T> implements Modifier<T> {
    protected final T entity;
    protected final D data;

    public ModifierImpl(@NotNull T entity, @NotNull CompiledRoute route, @NotNull D data, @NotNull Consumer<JsonObject> updater) {
        super(((ObeliskImpl) entity.getAPI()), route, (request, response) -> {
            // TODO: handle errors
            JsonObject content = response.getContent().getAsJsonObject();
            updater.accept(content);
            return entity;
        });
        this.entity = entity;
        this.data = data;
    }

    @Override
    public final @NotNull T getEntity() {
        return this.entity;
    }

    @Override
    public final @NotNull JsonElement getContent() {
        return this.data.toJson();
    }
}
