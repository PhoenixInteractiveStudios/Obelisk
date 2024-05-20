package org.burrow_studios.obelisk.monolith.action.entity.ticket;

import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseGetAction;
import org.jetbrains.annotations.NotNull;

public class DatabaseTicketGetAction extends DatabaseGetAction<Ticket> {
    public DatabaseTicketGetAction(@NotNull ObeliskMonolith obelisk, long id) {
        super(obelisk, id);
    }
}
