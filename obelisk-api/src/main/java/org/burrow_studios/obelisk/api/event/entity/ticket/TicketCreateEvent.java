package org.burrow_studios.obelisk.api.event.entity.ticket;

import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public final class TicketCreateEvent extends TicketEvent implements EntityDeleteEvent<Ticket> {
    public TicketCreateEvent(@NotNull Ticket entity) {
        super(entity);
    }
}
