package org.burrow_studios.obelisk.core.action;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.Builder;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.net.http.CompiledEndpoint;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public abstract class BuilderImpl<T extends Turtle> extends ActionImpl<T> implements Builder<T> {
    private final @NotNull Class<T> type;
    protected final @NotNull EntityData data;

    public BuilderImpl(
            @NotNull ObeliskImpl api,
            @NotNull Class<T> type,
            @NotNull CompiledEndpoint endpoint,
            @NotNull BiFunction<ObeliskImpl, EntityData, T> func
    ) {
        super(api, endpoint, (request, response) -> {
            // TODO: checks (error responses)
            final JsonObject content = response.getContent().getAsJsonObject();
            EntityData trustedData = new EntityData();

            final @NotNull T entity = func.apply(api, trustedData);
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
