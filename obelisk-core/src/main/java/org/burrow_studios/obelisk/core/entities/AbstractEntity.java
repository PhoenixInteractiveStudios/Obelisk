package org.burrow_studios.obelisk.core.entities;

import org.burrow_studios.obelisk.api.entities.IEntity;
import org.burrow_studios.obelisk.core.AbstractObelisk;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractEntity implements IEntity {
    protected final @NotNull AbstractObelisk obelisk;
    protected final long id;

    protected AbstractEntity(@NotNull AbstractObelisk obelisk, long id) {
        this.obelisk = obelisk;
        this.id = id;
    }

    @Override
    public final @NotNull AbstractObelisk getAPI() {
        return this.obelisk;
    }

    @Override
    public final long getId() {
        return this.id;
    }
}
