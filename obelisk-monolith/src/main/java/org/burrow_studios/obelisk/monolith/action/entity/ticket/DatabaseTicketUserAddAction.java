package org.burrow_studios.obelisk.monolith.action.entity.ticket;

import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseAction;
import org.burrow_studios.obelisk.monolith.db.interfaces.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendTicket;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DatabaseTicketUserAddAction extends DatabaseAction<Void> {
    private final Ticket ticket;
    private final User user;

    public DatabaseTicketUserAddAction(@NotNull BackendTicket ticket, @NotNull User user) {
        super(((ObeliskMonolith) ticket.getAPI()));
        this.ticket = ticket;
        this.user = user;
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<Void> future) throws DatabaseException {
        actionableDatabase.addTicketUser(this);
        future.complete(null);
    }

    public @NotNull Ticket getTicket() {
        return this.ticket;
    }

    public @NotNull User getUser() {
        return this.user;
    }
}
