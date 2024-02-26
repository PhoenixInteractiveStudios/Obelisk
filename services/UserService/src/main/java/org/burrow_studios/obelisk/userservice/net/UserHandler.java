package org.burrow_studios.obelisk.userservice.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.Status;
import org.burrow_studios.obelisk.commons.rpc.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.commons.turtle.TurtleGenerator;
import org.burrow_studios.obelisk.userservice.UserService;
import org.burrow_studios.obelisk.userservice.database.UserDB;
import org.burrow_studios.obelisk.userservice.exceptions.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

public class UserHandler {
    private final TurtleGenerator idGenerator = TurtleGenerator.get("UserService");
    private final UserService service;

    public UserHandler(@NotNull UserService service) {
        this.service = service;
    }

    public void onGetAll(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final JsonArray responseBody = new JsonArray();
        final Set<Long> userIds = getDatabase().getUserIds();

        for (Long userId : userIds)
            responseBody.add(userId);

        response.setStatus(Status.OK);
        response.setBody(responseBody);
    }

    public void onGet(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long userId = request.getPathSegmentAsLong(1);

        try {
            final JsonObject responseBody = getDatabase().getUser(userId);

            response.setStatus(Status.OK);
            response.setBody(responseBody);
        } catch (NoSuchEntryException e) {
            response.setStatus(Status.NOT_FOUND);
        }
    }

    public void onCreate(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final String name = request.bodyHelper().requireElementAsString("name");

        final long id = idGenerator.newId();
        getDatabase().createUser(id, name);

        final JsonObject responseBody = getDatabase().getUser(id);

        response.setStatus(Status.CREATED);
        response.setBody(responseBody);
    }

    public void onAddDiscord(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long userId    = request.getPathSegmentAsLong(1);
        final long snowflake = request.getPathSegmentAsLong(3);

        getDatabase().addUserDiscordId(userId, snowflake);

        response.setStatus(Status.NO_CONTENT);
    }

    public void onDelDiscord(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long userId    = request.getPathSegmentAsLong(1);
        final long snowflake = request.getPathSegmentAsLong(3);

        getDatabase().removeUserDiscordId(userId, snowflake);

        response.setStatus(Status.NO_CONTENT);
    }

    public void onAddMinecraft(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long userId = request.getPathSegmentAsLong(1);
        final UUID uuid   = request.getPathSegment(3, UUID::fromString);

        getDatabase().addUserMinecraftId(userId, uuid);

        response.setStatus(Status.NO_CONTENT);
    }

    public void onDelMinecraft(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long userId = request.getPathSegmentAsLong(1);
        final UUID uuid   = request.getPathSegment(3, UUID::fromString);

        getDatabase().removeUserMinecraftId(userId, uuid);

        response.setStatus(Status.NO_CONTENT);
    }

    public void onDelete(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long userId = request.getPathSegmentAsLong(1);

        getDatabase().deleteUser(userId);

        response.setStatus(Status.NO_CONTENT);
    }

    public void onEdit(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long userId = request.getPathSegmentAsLong(1);

        request.bodyHelper().optionalElementAsString("name").ifPresent(name -> {
            getDatabase().updateUserName(userId, name);
        });

        final JsonObject responseBody = getDatabase().getUser(userId);

        response.setStatus(Status.OK);
        response.setBody(responseBody);
    }

    public @NotNull UserDB getDatabase() {
        return this.service.getUserDB();
    }
}
