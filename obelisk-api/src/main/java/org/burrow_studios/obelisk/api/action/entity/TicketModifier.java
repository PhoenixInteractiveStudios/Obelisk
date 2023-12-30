package org.burrow_studios.obelisk.api.action.entity;

import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TicketModifier extends Modifier<Ticket> {
    @NotNull TicketModifier setTitle(@Nullable String title);

    @NotNull TicketModifier setState(@NotNull Ticket.State state);

    @NotNull TicketModifier addTags(@NotNull String... tags);

    @NotNull TicketModifier removeTags(@NotNull String... tags);

    @NotNull TicketModifier addUsers(@NotNull User... users);

    @NotNull TicketModifier removeUsers(@NotNull User... users);
}
