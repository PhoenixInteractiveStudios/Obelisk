package org.burrow_studios.obelisk.api.event.entity;

import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.api.event.Event;
import org.jetbrains.annotations.NotNull;

public interface EntityUpdateEvent<E extends Turtle, T> extends Event {
    @NotNull E getEntity();

    T getOldValue();

    T getNewValue();
}
