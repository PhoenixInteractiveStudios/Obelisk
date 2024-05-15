package org.burrow_studios.obelisk.api.event.entity;

import org.burrow_studios.obelisk.api.entities.IEntity;
import org.burrow_studios.obelisk.api.event.AbstractEvent;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractEntityEvent<E extends IEntity> extends AbstractEvent implements EntityEvent<E> {
    protected final @NotNull E entity;

    protected AbstractEntityEvent(long id, @NotNull E entity) {
        super(entity.getAPI(), id);
        this.entity = entity;
    }

    @Override
    public @NotNull E getEntity() {
        return this.entity;
    }
}
