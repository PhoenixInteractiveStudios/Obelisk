package org.burrow_studios.obelisk.server.net.http.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.entities.action.ticket.TicketBuilderImpl;
import org.burrow_studios.obelisk.core.entities.action.ticket.TicketModifierImpl;
import org.burrow_studios.obelisk.core.entities.impl.TicketImpl;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.burrow_studios.obelisk.server.net.http.Request;
import org.burrow_studios.obelisk.server.net.http.ResponseBuilder;
import org.burrow_studios.obelisk.server.net.http.exceptions.APIException;
import org.burrow_studios.obelisk.server.net.http.exceptions.BadRequestException;
import org.burrow_studios.obelisk.server.net.http.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.server.net.http.exceptions.NotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class TicketHandler {
    private final @NotNull NetworkHandler networkHandler;

    public TicketHandler(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void onGetAll(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        JsonArray json = new JsonArray();
        for (long id : getAPI().getTickets().getIdsAsImmutaleSet())
            json.add(id);

        response.setCode(200);
        response.setBody(json);
    }

    public void onGet(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long ticketId = request.getSegmentLong(1);

        final TicketImpl ticket = getAPI().getTicket(ticketId);
        if (ticket == null)
            throw new NotFoundException();

        response.setCode(200);
        response.setBody(ticket.toJson());
    }

    public void onCreate(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        Optional<JsonObject> body = request.optionalBody()
                .map(JsonElement::getAsJsonObject);

        if (body.isEmpty())
            throw new BadRequestException("No content");

        final JsonObject json = body.get();
        final JsonObject result;

        final TicketBuilderImpl builder;

        try {
            final String title = json.get("title").getAsString();

            final String stateStr = json.get("state").getAsString();
            final Ticket.State state = Ticket.State.valueOf(stateStr);

            builder = getAPI().createTicket()
                    .setTitle(title)
                    .setState(state);

            JsonArray users = json.getAsJsonArray("users");
            StreamSupport.stream(users.spliterator(), false)
                    .map(JsonElement::getAsLong)
                    .map(id -> {
                        UserImpl user = getAPI().getUser(id);
                        if (user == null)
                            throw new IllegalArgumentException("Invalid user id: " + id);
                        return user;
                    })
                    .forEach(builder::addUsers);

            JsonArray tags = json.getAsJsonArray("tags");
            StreamSupport.stream(tags.spliterator(), false)
                    .map(JsonElement::getAsString)
                    .forEach(builder::addTags);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Malformed body");
        }

        try {
            result = ((TicketImpl) builder.await()).toJson();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(result);
    }

    public void onAddUser(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long ticketId = request.getSegmentLong(1);
        final long   userId = request.getSegmentLong(3);

        final TicketImpl ticket = getAPI().getTicket(ticketId);
        final UserImpl   user   = getAPI().getUser(userId);

        if (ticket == null || user == null)
            throw new NotFoundException();

        try {
            ticket.addUser(user).await();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        response.setCode(204);
    }

    public void onDeleteUser(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long ticketId = request.getSegmentLong(1);
        final long   userId = request.getSegmentLong(3);

        final TicketImpl ticket = getAPI().getTicket(ticketId);
        final UserImpl   user   = getAPI().getUser(userId);

        if (ticket == null)
            throw new NotFoundException();

        if (user != null && ticket.getUsers().containsId(userId)) {
            try {
                ticket.removeUser(user).await();
            } catch (Exception e) {
                throw new InternalServerErrorException();
            }
        }

        response.setCode(204);
    }

    public void onDelete(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long ticketId = request.getSegmentLong(1);
        final TicketImpl ticket = getAPI().getTicket(ticketId);

        if (ticket != null) {
            try {
                ticket.delete().await();
            } catch (Exception e) {
                throw new InternalServerErrorException();
            }
        }

        response.setCode(204);
    }

    public void onEdit(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long ticketId = request.getSegmentLong(1);

        Optional<JsonObject> body = request.optionalBody()
                .map(JsonElement::getAsJsonObject);

        if (body.isEmpty())
            throw new BadRequestException("No content");

        final JsonObject json = body.get();

        TicketImpl ticket = getAPI().getTicket(ticketId);
        if (ticket == null)
            throw new NotFoundException();
        final TicketModifierImpl modifier = ticket.modify();

        try {
            Optional.ofNullable(json.get("title"))
                    .map(JsonElement::getAsString)
                    .ifPresent(modifier::setTitle);

            Optional.ofNullable(json.get("state"))
                    .map(JsonElement::getAsString)
                    .map(Ticket.State::valueOf)
                    .ifPresent(modifier::setState);

            final List<String> tags = ticket.getTags();
            Optional.ofNullable(json.get("tags"))
                    .map(JsonElement::getAsJsonArray)
                    .map(elements -> {
                        List<String> tagStrings = new ArrayList<>(elements.size());
                        for (JsonElement element : elements)
                            tagStrings.add(element.getAsString());
                        return tagStrings;
                    })
                    .ifPresent(elements -> {
                        // add new tags
                        for (String element : elements)
                            if (!tags.contains(element))
                                modifier.addTags(element);
                        // remove old tags
                        for (String element : tags)
                            if (!elements.contains(element))
                                modifier.removeTags(element);
                    });
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Malformed body");
        }

        try {
            ticket = ((TicketImpl) modifier.await());
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(ticket.toJson());
    }

    private @NotNull ObeliskImpl getAPI() {
        return this.networkHandler.getServer().getAPI();
    }
}
