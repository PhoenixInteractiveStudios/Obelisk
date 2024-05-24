package org.burrow_studios.obelisk.monolith.action.entity.ticket;

import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseDeleteAction;
import org.burrow_studios.obelisk.monolith.db.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendTicket;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DatabaseTicketDeleteAction extends DatabaseDeleteAction<Ticket> {
    public DatabaseTicketDeleteAction(@NotNull BackendTicket ticket) {
        super(((ObeliskMonolith) ticket.getAPI()), ticket.getId(), Ticket.class);
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<Void> future) throws DatabaseException {
        actionableDatabase.deleteTicket(this);
        future.complete(null);
    }
}
