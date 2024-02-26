package org.burrow_studios.obelisk.bruno.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.bruno.Bruno;
import org.burrow_studios.obelisk.bruno.database.Database;
import org.burrow_studios.obelisk.bruno.exceptions.NoSuchEntryException;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.Status;
import org.burrow_studios.obelisk.commons.rpc.exceptions.NotFoundException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.commons.turtle.TurtleGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TagHandler {
    private final TurtleGenerator idGenerator = TurtleGenerator.get("Bruno");
    private final Bruno bruno;

    public TagHandler(@NotNull Bruno bruno) {
        this.bruno = bruno;
    }

    public void onGetAll(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long boardId = request.getPathSegmentAsLong(1);

        final JsonArray responseBody = new JsonArray();
        final Set<Long> tagIds;
        try {
            tagIds = getDatabase().getTagIds(boardId);
        } catch (NoSuchEntryException e) {
            throw new NotFoundException();
        }

        for (Long tagId : tagIds)
            responseBody.add(tagId);

        response.setStatus(Status.OK);
        response.setBody(responseBody);
    }

    public void onGet(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long boardId = request.getPathSegmentAsLong(1);
        final long   tagId = request.getPathSegmentAsLong(3);

        try {
            final JsonObject responseBody = getDatabase().getTag(boardId, tagId);

            response.setStatus(Status.OK);
            response.setBody(responseBody);
        } catch (NoSuchEntryException e) {
            response.setStatus(Status.NOT_FOUND);
        }
    }

    public void onCreate(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long boardId = request.getPathSegmentAsLong(1);

        final String title = request.bodyHelper().requireElementAsString("title");

        final long id = idGenerator.newId();
        getDatabase().createTag(boardId, id, title);

        final JsonObject responseBody = getDatabase().getTag(boardId, id);

        response.setStatus(Status.CREATED);
        response.setBody(responseBody);
    }

    public void onDelete(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long boardId = request.getPathSegmentAsLong(1);
        final long   tagId = request.getPathSegmentAsLong(3);

        getDatabase().deleteTag(boardId, tagId);

        response.setStatus(Status.NO_CONTENT);
    }

    public void onEdit(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long boardId = request.getPathSegmentAsLong(1);
        final long   tagId = request.getPathSegmentAsLong(3);

        request.bodyHelper().optionalElementAsString("title").ifPresent(title -> {
            getDatabase().updateTagTitle(boardId, tagId, title);
        });

        final JsonObject responseBody = getDatabase().getTag(boardId, tagId);

        response.setStatus(Status.OK);
        response.setBody(responseBody);
    }

    private @NotNull Database getDatabase() {
        return this.bruno.getDatabase();
    }
}
