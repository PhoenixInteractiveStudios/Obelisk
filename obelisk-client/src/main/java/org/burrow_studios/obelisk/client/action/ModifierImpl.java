package org.burrow_studios.obelisk.client.action;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.IEntity;
import org.burrow_studios.obelisk.api.exceptions.ErrorResponseException;
import org.burrow_studios.obelisk.api.request.ErrorResponse;
import org.burrow_studios.obelisk.client.http.Response;
import org.burrow_studios.obelisk.core.AbstractObelisk;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;

public abstract class ModifierImpl<E extends IEntity> extends ActionImpl<E> implements Modifier<E> {
    protected final @NotNull E entity;
    protected final @NotNull JsonObject data;

    protected ModifierImpl(@NotNull E entity, @NotNull Route.Compiled route) {
        super(((AbstractObelisk) entity.getAPI()), route, (req, res) -> handle(entity, res));
        this.entity = entity;
        this.data = new JsonObject();
    }

    private static <T> @NotNull T handle(@NotNull T entity, @NotNull Response response) {
        if (response.getCode() != 200)
            throw new ErrorResponseException(ErrorResponse.SERVER_ERROR); // TODO: placeholder error

        // TODO: should updates already be applied to the cached entity?

        return entity;
    }

    @Override
    public final @NotNull E getEntity() {
        return this.entity;
    }

    @Override
    protected @NotNull JsonObject getRequestBody() {
        return this.data.deepCopy();
    }
}
