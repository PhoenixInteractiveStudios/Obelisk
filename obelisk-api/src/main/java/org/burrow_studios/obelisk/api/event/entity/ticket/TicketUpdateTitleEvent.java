package org.burrow_studios.obelisk.api.event.entity.ticket;

import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.event.GatewayOpcodes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TicketUpdateTitleEvent extends TicketUpdateEvent<String> {
    public TicketUpdateTitleEvent(long id, @NotNull Ticket entity, @Nullable String oldValue, @Nullable String newValue) {
        super(id, entity, oldValue, newValue);
    }

    @Override
    public int getOpcode() {
        return GatewayOpcodes.TICKET_UPDATE_TITLE_EVENT;
    }
}
