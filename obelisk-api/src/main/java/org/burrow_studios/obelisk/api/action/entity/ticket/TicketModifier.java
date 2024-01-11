package org.burrow_studios.obelisk.api.action.entity.ticket;

import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TicketModifier extends Modifier<Ticket> {
    @NotNull TicketModifier setTitle(@Nullable String title) throws IllegalArgumentException;

    @NotNull TicketModifier setState(@NotNull Ticket.State state);

    @NotNull TicketModifier setTags(@NotNull String... tags);

    @NotNull TicketModifier addTags(@NotNull String... tags);

    @NotNull TicketModifier removeTags(@NotNull String... tags);
}
