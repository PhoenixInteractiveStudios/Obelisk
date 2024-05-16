package org.burrow_studios.obelisk.client.action;

import com.google.gson.JsonElement;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.entities.IEntity;
import org.burrow_studios.obelisk.api.exceptions.ErrorResponseException;
import org.burrow_studios.obelisk.api.request.ErrorResponse;
import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.client.http.Response;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeleteActionImpl<E extends IEntity> extends ActionImpl<Void> implements DeleteAction<E> {
    private final long id;
    private final Class<E> type;

    public DeleteActionImpl(@NotNull ObeliskImpl obelisk, @NotNull Route.Compiled route, long id, @NotNull Class<E> type) {
        super(obelisk, route, (req, res) -> handle(res));
        this.id = id;
        this.type = type;
    }

    private static Void handle(@NotNull Response response) {
        if (response.getCode() != 200)
            throw new ErrorResponseException(ErrorResponse.SERVER_ERROR); // TODO: placeholder error

        return null;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public @Nullable Class<E> getType() {
        return this.type;
    }

    @Override
    protected @Nullable JsonElement getRequestBody() {
        return null;
    }
}
