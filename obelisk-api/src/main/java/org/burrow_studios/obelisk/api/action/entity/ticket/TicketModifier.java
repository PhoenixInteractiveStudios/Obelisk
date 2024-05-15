package org.burrow_studios.obelisk.api.action.entity.ticket;

import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TicketModifier extends Modifier<Ticket> {
    @NotNull TicketModifier setTitle(@Nullable String title);

    @NotNull TicketModifier setState(@NotNull Ticket.State state);
}
