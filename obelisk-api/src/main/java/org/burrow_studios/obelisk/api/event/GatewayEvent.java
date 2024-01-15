package org.burrow_studios.obelisk.api.event;

import org.burrow_studios.obelisk.api.event.entity.EntityEvent;

public sealed interface GatewayEvent extends Event permits AbstractEvent, EntityEvent {

}
