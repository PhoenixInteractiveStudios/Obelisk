package org.burrow_studios.obelisk.api.event.entity.ticket;

import org.burrow_studios.obelisk.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;

public final class TicketUpdateStateEvent extends TicketUpdateEvent<Ticket.State> {
    public TicketUpdateStateEvent(@NotNull Ticket entity, @NotNull Ticket.State oldValue, @NotNull Ticket.State newValue) {
        super(entity, oldValue, newValue);
    }
}
