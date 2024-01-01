package org.burrow_studios.obelisk.api.entities;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.ticket.TicketModifier;
import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.burrow_studios.obelisk.internal.entities.TicketImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public sealed interface Ticket extends Turtle permits TicketImpl {
    @Override
    @NotNull TicketModifier modify();

    @Override
    @NotNull DeleteAction<Ticket> delete();

    @Nullable String getTitle();

    @NotNull State getState();

    @NotNull List<String> getTags();

    @NotNull TurtleSetView<? extends User> getUsers();

    enum State {
        OPEN,
        RESOLVED,
        FROZEN,
        CLOSED,
        DRAFT
    }
}
