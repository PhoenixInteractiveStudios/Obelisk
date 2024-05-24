package org.burrow_studios.obelisk.monolith.action.entity.ticket;

import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseGetAction;
import org.burrow_studios.obelisk.monolith.entities.BackendTicket;
import org.jetbrains.annotations.NotNull;

public class DatabaseTicketGetAction extends DatabaseGetAction<BackendTicket> {
    public DatabaseTicketGetAction(@NotNull ObeliskMonolith obelisk, long id) {
        super(obelisk, id);
    }
}
