package org.burrow_studios.obelisk.server.db.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.impl.TicketImpl;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.burrow_studios.obelisk.core.source.Request;
import org.burrow_studios.obelisk.core.source.Response;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.EntityProvider;
import org.burrow_studios.obelisk.server.db.NoSuchEntryException;
import org.burrow_studios.obelisk.server.db.RequestHandler;
import org.burrow_studios.obelisk.server.db.entity.TicketDB;
import org.burrow_studios.obelisk.common.turtle.TurtleGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public class TicketHandler implements RequestHandler {
    private final TurtleGenerator turtleGenerator;
    private final EntityProvider provider;

    public TicketHandler(@NotNull EntityProvider provider) {
        this.provider = provider;
        this.turtleGenerator = TurtleGenerator.get(provider.getClass().getSimpleName());
    }

    public @NotNull Response onGetAll(@NotNull Request request) {
        final JsonArray tickets = new JsonArray();
        final Set<Long> ticketIds;

        try {
            ticketIds = this.getDB().getTicketIds();
        } catch (DatabaseException e) {
            return request.respond(500, null);
        }

        for (long ticketId : ticketIds)
            tickets.add(ticketId);

        return request.respond(200, tickets);
    }

    public @NotNull Response onGet(@NotNull Request request) {
        final long ticketId = (long) request.getEndpoint().args()[1];
        final JsonObject ticket;

        try {
            ticket = this.getDB().getTicket(ticketId);
        } catch (NoSuchEntryException e) {
            return request.respond(404, null);
        } catch (DatabaseException e) {
            return request.respond(500, null);
        }

        return request.respond(200, ticket);
    }

    public @NotNull Response onCreate(@NotNull Request request) {
        final long id = this.turtleGenerator.newId();

        final JsonObject json = request.optionalContent()
                .map(JsonElement::getAsJsonObject)
                .orElse(null);

        if (json == null)
            return request.respond(400, null);

        json.addProperty("id", id);

        final TicketImpl ticket = new TicketImpl(provider.getAPI(), new EntityData(json));

        this.getDB().createTicket(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getState()
        );

        for (UserImpl member : ticket.getUsers())
            this.getDB().addTicketUser(ticket.getId(), member.getId());

        for (String tag : ticket.getTags())
            this.getDB().addTicketTag(ticket.getId(), tag);

        return request.respond(200, ticket.toJson());
    }

    public @NotNull Response onAddUser(@NotNull Request request) {
        final long ticketId = (long) request.getEndpoint().args()[1];
        final long   userId = (long) request.getEndpoint().args()[3];

        this.getDB().addTicketUser(ticketId, userId);

        return request.respond(204, null);
    }

    public @NotNull Response onDeleteUser(@NotNull Request request) {
        final long ticketId = (long) request.getEndpoint().args()[1];
        final long   userId = (long) request.getEndpoint().args()[3];

        this.getDB().removeTicketUser(ticketId, userId);

        return request.respond(204, null);
    }

    public @NotNull Response onDelete(@NotNull Request request) {
        final long ticketId = (long) request.getEndpoint().args()[1];

        this.getDB().deleteTicket(ticketId);

        return request.respond(204, null);
    }

    public @NotNull Response onEdit(@NotNull Request request) {
        final long ticketId = (long) request.getEndpoint().args()[1];
        final JsonObject json = request.optionalContent()
                .map(JsonElement::getAsJsonObject)
                .orElse(null);

        if (json == null)
            return request.respond(400, null);

        Optional.ofNullable(json.get("title"))
                .map(j -> j.isJsonNull() ? null : j.getAsString())
                .ifPresent(title -> getDB().updateTicketTitle(ticketId, title));

        Optional.ofNullable(json.get("state"))
                .map(JsonElement::getAsString)
                .map(Ticket.State::valueOf)
                .ifPresent(state -> getDB().updateTicketState(ticketId, state));

        final JsonArray tags = getDB().getTicket(ticketId).getAsJsonArray("tags");
        Optional.ofNullable(json.get("tags"))
                .map(JsonElement::getAsJsonArray)
                .ifPresent(elements -> {
                    // add new tags
                    for (JsonElement element : elements)
                        if (!tags.contains(element))
                            getDB().addTicketTag(ticketId, element.getAsString());
                    // remove old tags
                    for (JsonElement element : tags)
                        if (!elements.contains(element))
                            getDB().removeTicketTag(ticketId, element.getAsString());
                });

        final JsonObject ticket = getDB().getTicket(ticketId);
        return request.respond(200, ticket);
    }

    private @NotNull TicketDB getDB() {
        return this.provider.getTicketDB();
    }
}
