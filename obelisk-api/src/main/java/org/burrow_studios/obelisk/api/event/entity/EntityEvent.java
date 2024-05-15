package org.burrow_studios.obelisk.api.event.entity;

import org.burrow_studios.obelisk.api.entities.IEntity;
import org.burrow_studios.obelisk.api.event.Event;
import org.jetbrains.annotations.NotNull;

public interface EntityEvent<E extends IEntity> extends Event {
    @NotNull E getEntity();
}
