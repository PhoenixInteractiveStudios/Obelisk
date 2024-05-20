package org.burrow_studios.obelisk.monolith.action;

import org.burrow_studios.obelisk.api.entities.IEntity;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.jetbrains.annotations.NotNull;

public abstract class DatabaseGetAction<E extends IEntity> extends DatabaseAction<E> {
    protected final long id;

    protected DatabaseGetAction(@NotNull ObeliskMonolith obelisk, long id) {
        super(obelisk);
        this.id = id;
    }

    public long getId() {
        return this.id;
    }
}
