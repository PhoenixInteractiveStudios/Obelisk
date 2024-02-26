package org.burrow_studios.obelisk.moderationservice.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.function.ExceptionalFunction;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.Status;
import org.burrow_studios.obelisk.commons.rpc.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.commons.turtle.TurtleGenerator;
import org.burrow_studios.obelisk.moderationservice.ModerationService;
import org.burrow_studios.obelisk.moderationservice.database.TicketDB;
import org.burrow_studios.obelisk.moderationservice.database.TicketState;
import org.burrow_studios.obelisk.moderationservice.exceptions.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TicketHandler {
    private final TurtleGenerator idGenerator = TurtleGenerator.get("ModerationService");
    private final ModerationService service;

    private static final ExceptionalFunction<JsonElement, TicketState, ? extends Exception> STATE_MAPPER = json -> TicketState.valueOf(json.getAsString());

    public TicketHandler(@NotNull ModerationService service) {
        this.service = service;
    }

    public void onGetAll(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final JsonArray responseBody = new JsonArray();
        final Set<Long> ticketIds = getDatabase().getTicketIds();

        for (Long ticketId : ticketIds)
            responseBody.add(ticketId);

        response.setStatus(Status.OK);
        response.setBody(responseBody);
    }

    public void onGet(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long ticketId = request.getPathSegmentAsLong(1);

        try {
            final JsonObject responseBody = getDatabase().getTicket(ticketId);

            response.setStatus(Status.OK);
            response.setBody(responseBody);
        } catch (NoSuchEntryException e) {
            response.setStatus(Status.NOT_FOUND);
        }
    }

    public void onCreate(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final String      title = request.bodyHelper().requireElementAsString("title");
        final TicketState state = request.bodyHelper().requireElement("state", STATE_MAPPER);

        final long id = idGenerator.newId();
        getDatabase().createTicket(id, title, state);

        request.bodyHelper().optionalElement("tags", JsonElement::getAsJsonArray).ifPresent(tags -> {
            for (JsonElement tag : tags)
                getDatabase().addTicketTag(id, tag.getAsString());
        });

        final JsonObject responseBody = getDatabase().getTicket(id);

        response.setStatus(Status.CREATED);
        response.setBody(responseBody);
    }

    public void onAddUser(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long ticketId = request.getPathSegmentAsLong(1);
        final long   userId = request.getPathSegmentAsLong(3);

        getDatabase().addTicketUser(ticketId, userId);

        response.setStatus(Status.NO_CONTENT);
    }

    public void onDelUser(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long ticketId = request.getPathSegmentAsLong(1);
        final long   userId = request.getPathSegmentAsLong(3);

        getDatabase().removeTicketUser(ticketId, userId);

        response.setStatus(Status.NO_CONTENT);
    }

    public void onDelete(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long ticketId = request.getPathSegmentAsLong(1);

        getDatabase().deleteTicket(ticketId);

        response.setStatus(Status.NO_CONTENT);
    }

    public void onEdit(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long ticketId = request.getPathSegmentAsLong(1);

        request.bodyHelper().optionalElementAsString("title").ifPresent(title -> {
            getDatabase().updateTicketTitle(ticketId, title);
        });
        request.bodyHelper().optionalElement("state", STATE_MAPPER).ifPresent(state -> {
            getDatabase().updateTicketState(ticketId, state);
        });
        request.bodyHelper().optionalElement("tags", JsonElement::getAsJsonArray).ifPresent(newTags -> {
            JsonObject ticket = getDatabase().getTicket(ticketId);
            JsonArray oldTags = ticket.getAsJsonArray("tags");

            if (oldTags == null)
                oldTags = new JsonArray();

            for (JsonElement oldTag : oldTags)
                if (!newTags.contains(oldTag))
                    getDatabase().removeTicketTag(ticketId, oldTag.getAsString());

            for (JsonElement newTag : newTags)
                if (!oldTags.contains(newTag))
                    getDatabase().addTicketTag(ticketId, newTag.getAsString());
        });

        final JsonObject responseBody = getDatabase().getTicket(ticketId);

        response.setStatus(Status.OK);
        response.setBody(responseBody);
    }

    private @NotNull TicketDB getDatabase() {
        return this.service.getTicketDB();
    }
}
