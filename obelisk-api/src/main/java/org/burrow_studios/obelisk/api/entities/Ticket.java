package org.burrow_studios.obelisk.api.entities;

import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.burrow_studios.obelisk.internal.entities.TicketImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public sealed interface Ticket extends Turtle permits TicketImpl {
    @Nullable String getTitle();

    @NotNull State getState();

    @NotNull List<String> getTags();

    @NotNull Set<Long> getUserIds();

    @NotNull TurtleSetView<? extends User> getUsers();

    enum State {
        OPEN,
        RESOLVED,
        FROZEN,
        CLOSED,
        DRAFT
    }
}
