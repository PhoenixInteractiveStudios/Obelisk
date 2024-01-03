package org.burrow_studios.obelisk.api.event.entity;

import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.api.event.Event;
import org.jetbrains.annotations.NotNull;

public interface EntityCreateEvent<E extends Turtle> extends Event {
    @NotNull E getEntity();
}
