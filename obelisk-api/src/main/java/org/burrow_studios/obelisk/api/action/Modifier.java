package org.burrow_studios.obelisk.api.action;

import org.burrow_studios.obelisk.api.entities.IEntity;
import org.jetbrains.annotations.NotNull;

public interface Modifier<E extends IEntity> extends Action<E> {
    @NotNull E getEntity();
}
