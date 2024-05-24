package org.burrow_studios.obelisk.monolith.action.entity.ticket;

import org.burrow_studios.obelisk.core.cache.EntityCache;
import org.burrow_studios.obelisk.core.entities.AbstractTicket;
import org.burrow_studios.obelisk.monolith.action.DatabaseListAction;
import org.jetbrains.annotations.NotNull;

public class DatabaseTicketListAction extends DatabaseListAction<AbstractTicket> {
    public DatabaseTicketListAction(@NotNull EntityCache<AbstractTicket> cache) {
        super(cache);
    }
}
