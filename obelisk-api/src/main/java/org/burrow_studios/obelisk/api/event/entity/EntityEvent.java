package org.burrow_studios.obelisk.api.event.entity;

import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.api.event.Event;
import org.burrow_studios.obelisk.api.event.GatewayEvent;
import org.jetbrains.annotations.NotNull;

public sealed interface EntityEvent<E extends Turtle> extends Event, GatewayEvent permits AbstractEntityEvent, EntityCreateEvent, EntityDeleteEvent, EntityUpdateEvent {
    @NotNull E getEntity();
}
