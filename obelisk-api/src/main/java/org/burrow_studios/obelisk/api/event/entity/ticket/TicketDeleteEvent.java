package org.burrow_studios.obelisk.api.event.entity.ticket;

import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public final class TicketDeleteEvent extends TicketEvent implements EntityDeleteEvent<Ticket> {
    public TicketDeleteEvent(@NotNull Ticket entity) {
        super(entity);
    }
}
