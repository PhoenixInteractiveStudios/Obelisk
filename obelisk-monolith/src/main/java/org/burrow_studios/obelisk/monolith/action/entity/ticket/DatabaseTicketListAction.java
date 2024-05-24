package org.burrow_studios.obelisk.monolith.action.entity.ticket;

import org.burrow_studios.obelisk.core.cache.EntityCache;
import org.burrow_studios.obelisk.core.entities.AbstractTicket;
import org.burrow_studios.obelisk.monolith.action.DatabaseListAction;
import org.burrow_studios.obelisk.monolith.db.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendTicket;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DatabaseTicketListAction extends DatabaseListAction<AbstractTicket> {
    public DatabaseTicketListAction(@NotNull EntityCache<AbstractTicket> cache) {
        super(cache);
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<EntityCache<AbstractTicket>> future) throws DatabaseException {
        List<BackendTicket> tickets = actionableDatabase.getTickets(this);

        this.getCache().clear();
        for (BackendTicket ticket : tickets)
            this.getCache().add(ticket);

        future.complete(this.getCache());
    }
}
