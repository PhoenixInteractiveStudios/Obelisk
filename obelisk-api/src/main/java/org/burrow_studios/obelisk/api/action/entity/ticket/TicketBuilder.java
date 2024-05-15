package org.burrow_studios.obelisk.api.action.entity.ticket;

import org.burrow_studios.obelisk.api.action.Builder;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TicketBuilder extends Builder<Ticket> {
    @NotNull TicketBuilder setTitle(@Nullable String title);

    @NotNull TicketBuilder setState(@NotNull Ticket.State state);
}
