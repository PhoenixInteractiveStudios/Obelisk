package org.burrow_studios.obelisk.monolith.http.handlers;

import org.burrow_studios.obelisk.core.entities.AbstractTicket;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.http.Request;
import org.burrow_studios.obelisk.monolith.http.Response;
import org.burrow_studios.obelisk.monolith.http.exceptions.NotFoundException;
import org.burrow_studios.obelisk.monolith.http.exceptions.RequestHandlerException;
import org.jetbrains.annotations.NotNull;

public class TicketHandler {
    private final ObeliskMonolith obelisk;

    public TicketHandler(@NotNull ObeliskMonolith obelisk) {
        this.obelisk = obelisk;
    }

    public @NotNull Response onGet(@NotNull Request request) throws RequestHandlerException {
        final long ticketId = request.parsePathSegment(1, Long::parseLong);

        AbstractTicket ticket = this.obelisk.getTicket(ticketId);

        if (ticket == null) {
            // TODO: query database?
        }

        if (ticket == null)
            throw new NotFoundException("Ticket not found");

        return new Response.Builder()
                .setBody(ticket.toJson())
                .setStatus(200)
                .build();
    }
}
