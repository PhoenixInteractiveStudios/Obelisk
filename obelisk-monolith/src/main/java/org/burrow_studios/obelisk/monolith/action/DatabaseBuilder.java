package org.burrow_studios.obelisk.monolith.action;

import org.burrow_studios.obelisk.api.action.Builder;
import org.burrow_studios.obelisk.api.entities.IEntity;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.jetbrains.annotations.NotNull;

public abstract class DatabaseBuilder<E extends IEntity> extends DatabaseAction<E> implements Builder<E> {
    protected DatabaseBuilder(@NotNull ObeliskMonolith obelisk) {
        super(obelisk);
    }
}
