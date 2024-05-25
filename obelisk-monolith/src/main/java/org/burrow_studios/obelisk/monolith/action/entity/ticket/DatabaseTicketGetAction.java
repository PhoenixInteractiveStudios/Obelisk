package org.burrow_studios.obelisk.monolith.action.entity.ticket;

import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseGetAction;
import org.burrow_studios.obelisk.monolith.db.interfaces.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendTicket;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DatabaseTicketGetAction extends DatabaseGetAction<BackendTicket> {
    public DatabaseTicketGetAction(@NotNull ObeliskMonolith obelisk, long id) {
        super(obelisk, id);
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<BackendTicket> future) throws DatabaseException {
        BackendTicket ticket = actionableDatabase.getTicket(this);
        future.complete(ticket);
    }
}
