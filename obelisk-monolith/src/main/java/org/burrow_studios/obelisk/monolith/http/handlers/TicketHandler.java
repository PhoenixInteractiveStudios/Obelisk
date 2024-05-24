package org.burrow_studios.obelisk.monolith.http.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.entity.ticket.TicketModifier;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.core.entities.AbstractTicket;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.entity.ticket.DatabaseTicketBuilder;
import org.burrow_studios.obelisk.monolith.http.Request;
import org.burrow_studios.obelisk.monolith.http.Response;
import org.burrow_studios.obelisk.monolith.http.exceptions.BadRequestException;
import org.burrow_studios.obelisk.monolith.http.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.monolith.http.exceptions.NotFoundException;
import org.burrow_studios.obelisk.monolith.http.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.util.Pipe;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;

public class TicketHandler {
    private final ObeliskMonolith obelisk;

    public TicketHandler(@NotNull ObeliskMonolith obelisk) {
        this.obelisk = obelisk;
    }

    public @NotNull Response onList(@NotNull Request request) throws RequestHandlerException {
        JsonArray arr = new JsonArray();

        this.obelisk.getTickets().forEach(ticket -> arr.add(ticket.toMinimalJson()));

        return new Response.Builder()
                .setStatus(200)
                .setBody(arr)
                .build();
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

    public @NotNull Response onPost(@NotNull Request request) throws RequestHandlerException {
        JsonObject requestJson = request.requireBodyObject();

        DatabaseTicketBuilder builder = this.obelisk.createTicket();

        Pipe.of(requestJson.get("title"), BadRequestException::new)
                .map(json -> {
                    if (json == null || json.isJsonNull())
                        return null;
                    return json.getAsString();
                }, "Malformed \"title\" attribute")
                .ifPresent(builder::setTitle);

        Ticket.State state = Pipe.of(requestJson.get("state"), BadRequestException::new)
                .nonNull("Missing \"state\" attribute")
                .map(JsonElement::getAsString, "Malformed \"state\" attribute")
                .map(Ticket.State::valueOf, "Malformed \"state\" attribute")
                .get();
        builder.setState(state);

        AbstractTicket ticket;
        try {
            ticket = ((AbstractTicket) builder.await());
        } catch (ExecutionException | InterruptedException e) {
            throw new InternalServerErrorException();
        }

        return new Response.Builder()
                .setBody(ticket.toJson())
                .setStatus(201)
                .build();
    }

    public @NotNull Response onPatch(@NotNull Request request) throws RequestHandlerException {
        final long ticketId = request.parsePathSegment(1, Long::parseLong);
        JsonObject requestJson = request.requireBodyObject();

        AbstractTicket ticket = this.obelisk.getTicket(ticketId);
        if (ticket == null)
            throw new NotFoundException("Ticket not found");

        TicketModifier modifier = ticket.modify();

        Pipe.of(requestJson.get("title"), BadRequestException::new)
                .map(json -> {
                    if (json == null)
                        return null;
                    if (json.isJsonNull()) {
                        modifier.setTitle(null);
                        return null;
                    }
                    return json.getAsString();
                }, "Malformed \"title\" attribute")
                .ifPresent(modifier::setTitle);

        Pipe.of(requestJson.get("state"), BadRequestException::new)
                .map(json -> {
                    if (json == null)
                        return null;
                    return Ticket.State.valueOf(json.getAsString());
                }, "Malformed \"state\" attribute")
                .ifPresent(modifier::setState);

        try {
            ticket = (AbstractTicket) modifier.await();
        } catch (ExecutionException | InterruptedException e) {
            throw new InternalServerErrorException();
        }

        return new Response.Builder()
                .setBody(ticket.toJson())
                .setStatus(200)
                .build();
    }

    public @NotNull Response onDelete(@NotNull Request request) throws RequestHandlerException {
        final long ticketId = request.parsePathSegment(1, Long::parseLong);

        Ticket ticket = this.obelisk.getTicket(ticketId);

        if (ticket != null)
            try {
                ticket.delete().await();
            } catch (ExecutionException | InterruptedException e) {
                throw new InternalServerErrorException();
            }

        return new Response.Builder()
                .setStatus(204)
                .build();
    }
}
