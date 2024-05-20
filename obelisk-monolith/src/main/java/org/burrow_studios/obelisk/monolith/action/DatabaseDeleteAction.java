package org.burrow_studios.obelisk.monolith.action;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.entities.IEntity;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DatabaseDeleteAction<E extends IEntity> extends DatabaseAction<Void> implements DeleteAction<E> {
    protected final long id;
    protected final Class<E> type;

    protected DatabaseDeleteAction(@NotNull ObeliskMonolith obelisk, long id, @NotNull Class<E> type) {
        super(obelisk);
        this.id = id;
        this.type = type;
    }

    @Override
    public final long getId() {
        return this.id;
    }

    @Override
    public final @Nullable Class<E> getType() {
        return this.type;
    }
}
