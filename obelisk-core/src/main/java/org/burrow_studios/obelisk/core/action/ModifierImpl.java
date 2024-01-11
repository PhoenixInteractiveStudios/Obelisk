package org.burrow_studios.obelisk.core.action;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.impl.TurtleImpl;
import org.burrow_studios.obelisk.core.net.http.CompiledRoute;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public abstract class ModifierImpl<T extends Turtle, I extends TurtleImpl> extends ActionImpl<T> implements Modifier<T> {
    protected final I entity;
    protected final EntityData data;

    public ModifierImpl(
            @NotNull I entity,
            @NotNull CompiledRoute route,
            @NotNull BiConsumer<EntityData, I> updater
    ) {
        super(entity.getAPI(), route, (request, response) -> {
            // TODO: handle errors
            final JsonObject content = response.getContent().getAsJsonObject();
            final EntityData updateData = new EntityData(content);

            updater.accept(updateData, entity);
            return (T) entity;
        });
        this.entity = entity;
        this.data = new EntityData();
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
