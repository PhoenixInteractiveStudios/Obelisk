package org.burrow_studios.obelisk.internal.action;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.Builder;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.data.Data;
import org.burrow_studios.obelisk.internal.entities.TurtleImpl;
import org.burrow_studios.obelisk.internal.net.http.CompiledRoute;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public abstract class BuilderImpl<T extends Turtle, I extends TurtleImpl<T>, D extends Data<I>> extends ActionImpl<T> implements Builder<T> {
    private final @NotNull Class<T> type;
    protected final @NotNull D data;

    public BuilderImpl(
            @NotNull ObeliskImpl api,
            @NotNull Class<T> type,
            @NotNull CompiledRoute route,
            @NotNull D data,
            @NotNull Function<JsonObject, D> responseBuilder
    ) {
        super(api, route, (request, response) -> {
            // TODO: checks (error responses)
            final JsonObject content = response.getContent().getAsJsonObject();
            final D responseData = responseBuilder.apply(content);

            final @NotNull I entity = responseData.build(api);
            // TODO: fire events
            return (T) entity;
        });
        this.type = type;
        this.data = data;
    }

    @Override
    public final @NotNull JsonElement getContent() {
        return data.toJson();
    }
}
