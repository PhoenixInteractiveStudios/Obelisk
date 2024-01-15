package org.burrow_studios.obelisk.api.event.entity.ticket;

import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract sealed class TicketEvent extends EntityEvent<Ticket> permits TicketCreateEvent, TicketDeleteEvent, TicketUpdateEvent {
    protected TicketEvent(long id, @NotNull Ticket entity) {
        super(id, entity);
    }
}
