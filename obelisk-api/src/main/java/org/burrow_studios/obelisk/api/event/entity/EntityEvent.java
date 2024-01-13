package org.burrow_studios.obelisk.api.event.entity;

import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.api.event.AbstractEvent;
import org.jetbrains.annotations.NotNull;

public abstract class EntityEvent<E extends Turtle> extends AbstractEvent {
    protected final @NotNull E entity;

    protected EntityEvent(long id, @NotNull E entity) {
        super(entity.getAPI(), id);
        this.entity = entity;
    }

    public final @NotNull E getEntity() {
        return this.entity;
    }
}
