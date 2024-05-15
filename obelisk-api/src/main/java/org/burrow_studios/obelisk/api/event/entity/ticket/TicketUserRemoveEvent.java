package org.burrow_studios.obelisk.api.event.entity.ticket;

import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

public class TicketUserRemoveEvent extends TicketEvent {
    private final @NotNull User user;

    public TicketUserRemoveEvent(long id, @NotNull Ticket entity, @NotNull User user) {
        super(id, entity);
        this.user = user;
    }

    public @NotNull User getUser() {
        return user;
    }
}
