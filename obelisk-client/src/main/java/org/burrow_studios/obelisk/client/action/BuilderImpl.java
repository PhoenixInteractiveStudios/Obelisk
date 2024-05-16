package org.burrow_studios.obelisk.client.action;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.Builder;
import org.burrow_studios.obelisk.api.entities.IEntity;
import org.burrow_studios.obelisk.api.exceptions.ErrorResponseException;
import org.burrow_studios.obelisk.api.request.ErrorResponse;
import org.burrow_studios.obelisk.client.EntityBuilder;
import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.client.http.Response;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public abstract class BuilderImpl<E extends IEntity> extends ActionImpl<E> implements Builder<E> {
    protected final @NotNull JsonObject data;

    protected BuilderImpl(@NotNull ObeliskImpl obelisk, @NotNull Route.Compiled route, @NotNull BiFunction<EntityBuilder, JsonObject, E> handler) {
        super(obelisk, route, (req, res) -> handle(obelisk, res, handler));
        this.data = new JsonObject();
    }

    private static <T> @NotNull T handle(@NotNull ObeliskImpl obelisk, @NotNull Response response, @NotNull BiFunction<EntityBuilder, JsonObject, T> handler) {
        if (response.getCode() != 201)
            throw new ErrorResponseException(ErrorResponse.SERVER_ERROR); // TODO: placeholder error

        EntityBuilder entityBuilder = obelisk.getEntityBuilder();

        JsonElement responseBodyElement = response.getBodyJson();
        if (!(responseBodyElement instanceof JsonObject responseBody))
            throw new ErrorResponseException(ErrorResponse.SERVER_ERROR); // TODO: placeholder error (probably wrong exception type)

        return handler.apply(entityBuilder, responseBody);
    }

    @Override
    protected @NotNull JsonObject getRequestBody() {
        return this.data.deepCopy();
    }
}
