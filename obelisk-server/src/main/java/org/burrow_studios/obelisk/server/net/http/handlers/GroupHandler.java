package org.burrow_studios.obelisk.server.net.http.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.entities.action.group.GroupBuilderImpl;
import org.burrow_studios.obelisk.core.entities.action.group.GroupModifierImpl;
import org.burrow_studios.obelisk.core.entities.impl.GroupImpl;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.burrow_studios.obelisk.server.net.http.Request;
import org.burrow_studios.obelisk.server.net.http.ResponseBuilder;
import org.burrow_studios.obelisk.server.net.http.exceptions.APIException;
import org.burrow_studios.obelisk.server.net.http.exceptions.BadRequestException;
import org.burrow_studios.obelisk.server.net.http.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.server.net.http.exceptions.NotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.stream.StreamSupport;

public class GroupHandler {
    private final @NotNull NetworkHandler networkHandler;

    public GroupHandler(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void onGetAll(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        JsonArray json = new JsonArray();
        for (long id : getAPI().getGroups().getIdsAsImmutaleSet())
            json.add(id);

        response.setCode(200);
        response.setBody(json);
    }

    public void onGet(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long groupId = request.getSegmentLong(1);

        final GroupImpl group = getAPI().getGroup(groupId);
        if (group == null)
            throw new NotFoundException();
        
        response.setCode(200);
        response.setBody(group.toJson());
    }

    public void onCreate(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        Optional<JsonObject> body = request.optionalBody()
                .map(JsonElement::getAsJsonObject);

        if (body.isEmpty())
            throw new BadRequestException("No content");

        final JsonObject json = body.get();
        final JsonObject result;

        final GroupBuilderImpl builder;

        try {
            final String name     = json.get("name").getAsString();
            final int    position = json.get("position").getAsInt();

            builder = getAPI().createGroup()
                    .setName(name)
                    .setPosition(position);

            JsonArray members = json.getAsJsonArray("members");
            StreamSupport.stream(members.spliterator(), false)
                    .map(JsonElement::getAsLong)
                    .map(id -> {
                        UserImpl user = getAPI().getUser(id);
                        if (user == null)
                            throw new IllegalArgumentException("Invalid user id: " + id);
                        return user;
                    })
                    .forEach(builder::addMembers);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Malformed body");
        }

        try {
            result = ((GroupImpl) builder.await()).toJson();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(result);
    }

    public void onAddMember(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long groupId = request.getSegmentLong(1);
        final long  userId = request.getSegmentLong(3);

        final GroupImpl group = getAPI().getGroup(groupId);
        final UserImpl  user  = getAPI().getUser(userId);

        if (group == null || user == null)
            throw new NotFoundException();

        try {
            group.addMember(user).await();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        response.setCode(204);
    }

    public void onDeleteMember(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long groupId = request.getSegmentLong(1);
        final long  userId = request.getSegmentLong(3);

        final GroupImpl group = getAPI().getGroup(groupId);
        final UserImpl  user  = getAPI().getUser(userId);

        if (group == null)
            throw new NotFoundException();

        if (user != null && group.getMembers().containsId(userId)) {
            try {
                group.removeMember(user).await();
            } catch (Exception e) {
                throw new InternalServerErrorException();
            }
        }

        response.setCode(204);
    }

    public void onDelete(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long groupId = request.getSegmentLong(1);
        final GroupImpl group = getAPI().getGroup(groupId);

        if (group != null) {
            try {
                group.delete().await();
            } catch (Exception e) {
                throw new InternalServerErrorException();
            }
        }

        response.setCode(204);
    }

    public void onEdit(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long groupId = request.getSegmentLong(1);

        Optional<JsonObject> body = request.optionalBody()
                .map(JsonElement::getAsJsonObject);

        if (body.isEmpty())
            throw new BadRequestException("No content");

        final JsonObject json = body.get();

        GroupImpl group = getAPI().getGroup(groupId);
        if (group == null)
            throw new NotFoundException();
        final GroupModifierImpl modifier = group.modify();

        try {
            Optional.ofNullable(json.get("name"))
                    .map(JsonElement::getAsString)
                    .ifPresent(modifier::setName);

            Optional.ofNullable(json.get("position"))
                    .map(JsonElement::getAsInt)
                    .ifPresent(modifier::setPosition);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Malformed body");
        }

        try {
            group = ((GroupImpl) modifier.await());
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(group.toJson());
    }

    private @NotNull ObeliskImpl getAPI() {
        return this.networkHandler.getServer().getAPI();
    }
}
