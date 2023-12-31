package org.burrow_studios.obelisk.internal.action;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.Builder;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.common.function.ExceptionalBiFunction;
import org.burrow_studios.obelisk.internal.EntityBuilder;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.data.Data;
import org.burrow_studios.obelisk.internal.net.http.CompiledRoute;
import org.jetbrains.annotations.NotNull;

public abstract class BuilderImpl<T extends Turtle, D extends Data<T>> extends ActionImpl<T> implements Builder<T> {
    private final @NotNull Class<T> type;
    protected final @NotNull D data;

    public BuilderImpl(
            @NotNull ObeliskImpl api,
            @NotNull Class<T> type,
            @NotNull CompiledRoute route,
            @NotNull D data,
            @NotNull ExceptionalBiFunction<EntityBuilder, JsonObject, T, ? extends Exception> builder
    ) {
        super(api, route, (request, response) -> {
            // TODO: checks (error responses)
            final T entity = builder.apply(api.getEntityBuilder(), response.getContent().getAsJsonObject());
            // TODO: fire events
            return entity;
        });
        this.type = type;
        this.data = data;
    }

    @Override
    public final @NotNull JsonElement getContent() {
        return data.toJson();
    }
}
