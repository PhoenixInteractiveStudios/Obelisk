package org.burrow_studios.obelisk.core.action;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.commons.http.CompiledEndpoint;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.jetbrains.annotations.NotNull;

public final class DeleteActionImpl<T extends Turtle> extends ActionImpl<Void> implements DeleteAction<T> {
    private final @NotNull Class<T> type;
    private final long entityId;

    public DeleteActionImpl(
            @NotNull ObeliskImpl api,
            @NotNull Class<T> type,
            long entityId,
            @NotNull CompiledEndpoint endpoint
    ) {
        super(api, endpoint, (request, response) -> {
            // TODO: throw exception if response is an error
            return null;
        });
        this.type = type;
        this.entityId = entityId;
    }

    @Override
    public long getId() {
        return this.entityId;
    }

    @Override
    public @NotNull Class<T> getType() {
        return this.type;
    }
}
