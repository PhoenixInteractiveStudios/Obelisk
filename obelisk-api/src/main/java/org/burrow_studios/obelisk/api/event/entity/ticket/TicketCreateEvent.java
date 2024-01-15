package org.burrow_studios.obelisk.api.event.entity.ticket;

import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.event.GatewayOpcodes;
import org.burrow_studios.obelisk.api.event.entity.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public final class TicketCreateEvent extends TicketEvent implements EntityCreateEvent<Ticket> {
    public TicketCreateEvent(long id, @NotNull Ticket entity) {
        super(id, entity);
    }

    @Override
    public int getOpcode() {
        return GatewayOpcodes.TICKET_CREATE_EVENT;
    }
}
