package org.burrow_studios.obelisk.bruno.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.bruno.Bruno;
import org.burrow_studios.obelisk.bruno.database.Database;
import org.burrow_studios.obelisk.bruno.exceptions.NoSuchEntryException;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.Status;
import org.burrow_studios.obelisk.commons.rpc.exceptions.BadRequestException;
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
        final String boardIdStr = request.getPath().split("/")[1];
        final long   boardId    = Long.parseLong(boardIdStr);

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
        final String[] pathSegments = request.getPath().split("/");
        final long boardId = Long.parseLong(pathSegments[1]);
        final long   tagId = Long.parseLong(pathSegments[3]);

        try {
            final JsonObject responseBody = getDatabase().getTag(boardId, tagId);

            response.setStatus(Status.OK);
            response.setBody(responseBody);
        } catch (NoSuchEntryException e) {
            response.setStatus(Status.NOT_FOUND);
        }
    }

    public void onCreate(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final String boardIdStr = request.getPath().split("/")[1];
        final long   boardId    = Long.parseLong(boardIdStr);

        if (!(request.getBody() instanceof JsonObject requestBody))
            throw new BadRequestException("Missing request body");

        if (!(requestBody.get("title") instanceof JsonPrimitive titleInfo))
            throw new BadRequestException("Malformed request body: Malformed title info");
        final String title = titleInfo.getAsString();

        final long id = idGenerator.newId();
        getDatabase().createTag(boardId, id, title);

        final JsonObject responseBody = getDatabase().getTag(boardId, id);

        response.setStatus(Status.CREATED);
        response.setBody(responseBody);
    }

    public void onDelete(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final String[] pathSegments = request.getPath().split("/");
        final long boardId = Long.parseLong(pathSegments[1]);
        final long   tagId = Long.parseLong(pathSegments[3]);

        getDatabase().deleteTag(boardId, tagId);

        response.setStatus(Status.NO_CONTENT);
    }

    public void onEdit(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final String[] pathSegments = request.getPath().split("/");
        final long boardId = Long.parseLong(pathSegments[1]);
        final long   tagId = Long.parseLong(pathSegments[3]);

        if (!(request.getBody() instanceof JsonObject requestBody))
            throw new BadRequestException("Missing request body");

        final JsonElement titleInfo = requestBody.get("title");
        if (titleInfo != null) {
            if (!(titleInfo instanceof JsonPrimitive json))
                throw new BadRequestException("Malformed request body: Malformed title info");

            final String title = json.getAsString();

            getDatabase().updateTagTitle(boardId, tagId, title);
        }

        final JsonObject responseBody = getDatabase().getTag(boardId, tagId);

        response.setStatus(Status.OK);
        response.setBody(responseBody);
    }

    private @NotNull Database getDatabase() {
        return this.bruno.getDatabase();
    }
}
