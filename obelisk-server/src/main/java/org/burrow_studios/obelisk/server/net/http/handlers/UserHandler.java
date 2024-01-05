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

public class UserHandler {
    private final @NotNull NetworkHandler networkHandler;

    public UserHandler(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void onGetAll(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        JsonArray json = new JsonArray();
        getUserService().getUsers().forEach(json::add);

        response.setCode(200);
        response.setBody(json);
    }

    public void onGet(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long userId = request.getSegmentLong(1);

        final JsonObject user;

        try {
            user = getUserService().getUser(userId);
        } catch (NoSuchEntryException e) {
            throw new NotFoundException();
        } catch (DatabaseException e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(user);
    }

    public void onCreate(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        Optional<JsonObject> body = request.optionalBody()
                .map(JsonElement::getAsJsonObject);

        if (body.isEmpty())
            throw new BadRequestException("No content");

        final JsonObject json = body.get();
        final JsonObject result;

        try {
            result = getUserService().createUser(json);
        } catch (DatabaseException e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(result);
    }

    public void onDelete(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long user = request.getSegmentLong(1);

        try {
            getUserService().deleteUser(user);
        } catch (DatabaseException e) {
            throw new InternalServerErrorException();
        }

        response.setCode(204);
    }

    public void onEdit(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        long user = request.getSegmentLong(1);

        Optional<JsonObject> body = request.optionalBody()
                .map(JsonElement::getAsJsonObject);

        if (body.isEmpty())
            throw new BadRequestException("No content");

        final JsonObject json = body.get();
        final JsonObject result;

        try {
            getUserService().patchUser(user, json);

            result = getUserService().getUser(user);
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
