package org.burrow_studios.obelisk.monolith.action;

import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.IEntity;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.jetbrains.annotations.NotNull;

public abstract class DatabaseModifier<E extends IEntity> extends DatabaseAction<E> implements Modifier<E> {
    protected final E entity;

    protected DatabaseModifier(@NotNull E entity) {
        super(((ObeliskMonolith) entity.getAPI()));
        this.entity = entity;
    }

    @Override
    public final @NotNull E getEntity() {
        return this.entity;
    }
}
