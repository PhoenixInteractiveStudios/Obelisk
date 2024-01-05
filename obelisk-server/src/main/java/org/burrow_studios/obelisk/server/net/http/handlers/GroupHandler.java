package org.burrow_studios.obelisk.server.net.http.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.NoSuchEntryException;
import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.burrow_studios.obelisk.server.net.http.Request;
import org.burrow_studios.obelisk.server.net.http.ResponseBuilder;
import org.burrow_studios.obelisk.server.net.http.exceptions.APIException;
import org.burrow_studios.obelisk.server.net.http.exceptions.BadRequestException;
import org.burrow_studios.obelisk.server.net.http.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.server.net.http.exceptions.NotFoundException;
import org.burrow_studios.obelisk.server.users.UserService;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class GroupHandler {
    private final @NotNull NetworkHandler networkHandler;

    public GroupHandler(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void onGetAll(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        JsonArray json = new JsonArray();
        getUserService().getGroups().forEach(json::add);

        response.setCode(200);
        response.setBody(json);
    }

    public void onGet(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long groupId = request.getSegmentLong(1);

        final JsonObject group;

        try {
            group = getUserService().getGroup(groupId);
        } catch (NoSuchEntryException e) {
            throw new NotFoundException();
        } catch (DatabaseException e) {
            throw new InternalServerErrorException();
        }
        
        response.setCode(200);
        response.setBody(group);
    }

    public void onCreate(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        Optional<JsonObject> body = request.optionalBody()
                .map(JsonElement::getAsJsonObject);

        if (body.isEmpty())
            throw new BadRequestException("No content");

        final JsonObject json = body.get();
        final JsonObject result;

        try {
            result = getUserService().createGroup(json);
        } catch (DatabaseException e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(result);
    }

    public void onAddMember(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long group = request.getSegmentLong(1);
        final long user  = request.getSegmentLong(3);

        try {
            getUserService().addGroupMember(group, user);
        } catch (DatabaseException e) {
            throw new InternalServerErrorException();
        }

        response.setCode(204);
    }

    public void onDeleteMember(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long group = request.getSegmentLong(1);
        final long user  = request.getSegmentLong(3);

        try {
            getUserService().removeGroupMember(group, user);
        } catch (DatabaseException e) {
            throw new InternalServerErrorException();
        }

        response.setCode(204);
    }

    public void onDelete(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long group = request.getSegmentLong(1);

        try {
            getUserService().deleteGroup(group);
        } catch (DatabaseException e) {
            throw new InternalServerErrorException();
        }

        response.setCode(204);
    }

    public void onEdit(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        long group = request.getSegmentLong(1);

        Optional<JsonObject> body = request.optionalBody()
                .map(JsonElement::getAsJsonObject);

        if (body.isEmpty())
            throw new BadRequestException("No content");

        final JsonObject json = body.get();
        final JsonObject result;

        try {
            getUserService().patchGroup(group, json);

            result = getUserService().getGroup(group);
        } catch (DatabaseException e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(result);
    }
    
    private @NotNull UserService getUserService() {
        return this.networkHandler.getServer().getUserService();
    }
}
