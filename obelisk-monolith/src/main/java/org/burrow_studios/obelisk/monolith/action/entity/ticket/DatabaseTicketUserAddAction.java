package org.burrow_studios.obelisk.monolith.action.entity.ticket;

import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseAction;
import org.burrow_studios.obelisk.monolith.entities.BackendTicket;
import org.jetbrains.annotations.NotNull;

public class DatabaseTicketUserAddAction extends DatabaseAction<Void> {
    private final User user;

    public DatabaseTicketUserAddAction(@NotNull BackendTicket ticket, @NotNull User user) {
        super(((ObeliskMonolith) ticket.getAPI()));
        this.user = user;
    }

    public @NotNull User getUser() {
        return this.user;
    }
}
