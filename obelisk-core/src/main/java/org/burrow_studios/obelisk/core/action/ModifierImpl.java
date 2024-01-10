package org.burrow_studios.obelisk.core.action;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.core.data.Data;
import org.burrow_studios.obelisk.core.entities.TurtleImpl;
import org.burrow_studios.obelisk.core.net.http.CompiledRoute;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public abstract class ModifierImpl<T extends Turtle, I extends TurtleImpl<T>, D extends Data<I>> extends ActionImpl<T> implements Modifier<T> {
    protected final I entity;
    protected final D data;

    public ModifierImpl(
            @NotNull I entity,
            @NotNull CompiledRoute route,
            @NotNull D data,
            @NotNull Function<JsonObject, D> responseBuilder
    ) {
        super(entity.getAPI(), route, (request, response) -> {
            // TODO: handle errors
            final JsonObject content = response.getContent().getAsJsonObject();
            final D responseData = responseBuilder.apply(content);

            responseData.update(entity);
            return (T) entity;
        });
        this.entity = entity;
        this.data = data;
    }

    @Override
    public final @NotNull T getEntity() {
        return (T) this.entity;
    }

    @Override
    public final @NotNull JsonElement getContent() {
        return this.data.toJson();
    }
}
