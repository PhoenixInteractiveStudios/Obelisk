package org.burrow_studios.obelisk.core.action;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.Builder;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.impl.TurtleImpl;
import org.burrow_studios.obelisk.core.net.http.CompiledRoute;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public abstract class BuilderImpl<T extends Turtle, I extends TurtleImpl<T>> extends ActionImpl<T> implements Builder<T> {
    private final @NotNull Class<T> type;
    protected final @NotNull EntityData data;

    public BuilderImpl(
            @NotNull ObeliskImpl api,
            @NotNull Class<T> type,
            @NotNull CompiledRoute route,
            @NotNull BiFunction<EntityData, ObeliskImpl, T> func
            ) {
        super(api, route, (request, response) -> {
            // TODO: checks (error responses)
            final JsonObject content = response.getContent().getAsJsonObject();
            EntityData trustedData = new EntityData();

            final @NotNull T entity = func.apply(trustedData, api);
            // TODO: fire events
            return entity;
        });
        this.type = type;
        this.data = new EntityData();
    }

    @Override
    public final @NotNull JsonElement getContent() {
        return data.toJson();
    }
}
