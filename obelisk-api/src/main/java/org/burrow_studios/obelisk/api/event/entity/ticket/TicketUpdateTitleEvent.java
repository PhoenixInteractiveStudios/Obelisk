package org.burrow_studios.obelisk.api.event.entity.ticket;

import org.burrow_studios.obelisk.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TicketUpdateTitleEvent extends TicketUpdateEvent<String> {
    public TicketUpdateTitleEvent(@NotNull Ticket entity, @Nullable String oldValue, @Nullable String newValue) {
        super(entity, oldValue, newValue);
    }
}
