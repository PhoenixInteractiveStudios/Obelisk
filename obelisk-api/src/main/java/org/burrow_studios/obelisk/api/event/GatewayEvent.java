package org.burrow_studios.obelisk.api.event;

import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.EntityUpdateEvent;

public sealed interface GatewayEvent extends Event permits AbstractEvent, EntityDeleteEvent, EntityUpdateEvent {

}
