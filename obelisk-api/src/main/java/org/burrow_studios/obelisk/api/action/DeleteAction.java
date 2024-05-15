package org.burrow_studios.obelisk.api.action;

import org.burrow_studios.obelisk.api.entities.IEntity;
import org.jetbrains.annotations.Nullable;

public interface DeleteAction<E extends IEntity> extends Action<Void> {
    long getId();

    @Nullable Class<E> getType();
}
