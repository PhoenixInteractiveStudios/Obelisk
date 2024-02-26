package org.burrow_studios.obelisk.userservice.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.Status;
import org.burrow_studios.obelisk.commons.rpc.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.commons.turtle.TurtleGenerator;
import org.burrow_studios.obelisk.userservice.UserService;
import org.burrow_studios.obelisk.userservice.database.GroupDB;
import org.burrow_studios.obelisk.userservice.exceptions.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class GroupHandler {
    private final TurtleGenerator idGenerator = TurtleGenerator.get("UserService");
    private final UserService service;

    public GroupHandler(@NotNull UserService service) {
        this.service = service;
    }

    public void onGetAll(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final JsonArray responseBody = new JsonArray();
        final Set<Long> groupIds = getDatabase().getGroupIds();

        for (Long groupId : groupIds)
            responseBody.add(groupId);

        response.setStatus(Status.OK);
        response.setBody(responseBody);
    }

    public void onGet(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long groupId = request.getPathSegmentAsLong(1);

        try {
            final JsonObject responseBody = getDatabase().getGroup(groupId);

            response.setStatus(Status.OK);
            response.setBody(responseBody);
        } catch (NoSuchEntryException e) {
            response.setStatus(Status.NOT_FOUND);
        }
    }

    public void onCreate(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final String name     = request.bodyHelper().requireElementAsString("name");
        final int    position = request.bodyHelper().requireElementAsInt("position");

        final long id = idGenerator.newId();
        getDatabase().createGroup(id, name, position);

        final JsonObject responseBody = getDatabase().getGroup(id);

        response.setStatus(Status.CREATED);
        response.setBody(responseBody);
    }

    public void onAddMember(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long groupId = request.getPathSegmentAsLong(1);
        final long  userId = request.getPathSegmentAsLong(3);

        getDatabase().addGroupMember(groupId, userId);

        response.setStatus(Status.NO_CONTENT);
    }

    public void onDelMember(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long groupId = request.getPathSegmentAsLong(1);
        final long  userId = request.getPathSegmentAsLong(3);

        getDatabase().removeGroupMember(groupId, userId);

        response.setStatus(Status.NO_CONTENT);
    }

    public void onDelete(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long groupId = request.getPathSegmentAsLong(1);

        getDatabase().deleteGroup(groupId);

        response.setStatus(Status.NO_CONTENT);
    }

    public void onEdit(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long groupId = request.getPathSegmentAsLong(1);

        request.bodyHelper().optionalElementAsString("name").ifPresent(name -> {
            getDatabase().updateGroupName(groupId, name);
        });
        request.bodyHelper().optionalElementAsInt("position").ifPresent(position -> {
            getDatabase().updateGroupPosition(groupId, position);
        });

        final JsonObject responseBody = getDatabase().getGroup(groupId);

        response.setStatus(Status.OK);
        response.setBody(responseBody);
    }

    private @NotNull GroupDB getDatabase() {
        return this.service.getGroupDB();
    }
}
