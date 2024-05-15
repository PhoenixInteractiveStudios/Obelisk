package org.burrow_studios.obelisk.api.event.entity.ticket;

import org.burrow_studios.obelisk.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TicketUpdateTitleEvent extends TicketUpdateEvent<String> {
    public TicketUpdateTitleEvent(long id, @NotNull Ticket entity, @Nullable String oldValue, @Nullable String newValue) {
        super(id, entity, oldValue, newValue);
    }
}
