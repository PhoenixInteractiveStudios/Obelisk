package org.burrow_studios.obelisk.monolith.action.entity.ticket;

import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseDeleteAction;
import org.burrow_studios.obelisk.monolith.entities.BackendTicket;
import org.jetbrains.annotations.NotNull;

public class DatabaseTicketDeleteAction extends DatabaseDeleteAction<Ticket> {
    public DatabaseTicketDeleteAction(@NotNull BackendTicket ticket) {
        super(((ObeliskMonolith) ticket.getAPI()), ticket.getId(), Ticket.class);
    }
}
