package org.burrow_studios.obelisk.monolith.action.entity.ticket;

import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseAction;
import org.burrow_studios.obelisk.monolith.entities.BackendTicket;
import org.jetbrains.annotations.NotNull;

public class DatabaseTicketUserRemoveAction extends DatabaseAction<Void> {
    private final Ticket ticket;
    private final User user;

    public DatabaseTicketUserRemoveAction(@NotNull BackendTicket ticket, @NotNull User user) {
        super(((ObeliskMonolith) ticket.getAPI()));
        this.ticket = ticket;
        this.user = user;
    }

    public @NotNull Ticket getTicket() {
        return this.ticket;
    }

    public @NotNull User getUser() {
        return this.user;
    }
}
