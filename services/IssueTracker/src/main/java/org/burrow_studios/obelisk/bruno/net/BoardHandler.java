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
import org.burrow_studios.obelisk.commons.rpc.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.commons.turtle.TurtleGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class BoardHandler {
    private final TurtleGenerator idGenerator = TurtleGenerator.get("Bruno");
    private final Bruno bruno;

    public BoardHandler(@NotNull Bruno bruno) {
        this.bruno = bruno;
    }

    public void onGetAll(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final JsonArray responseBody = new JsonArray();
        final Set<Long> boardIds = getDatabase().getBoardIds();

        for (Long boardId : boardIds)
            responseBody.add(boardId);

        response.setStatus(Status.OK);
        response.setBody(responseBody);
    }

    public void onGet(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long boardId = request.getPathSegmentAsLong(1);

        try {
            final JsonObject responseBody = getDatabase().getBoard(boardId);

            response.setStatus(Status.OK);
            response.setBody(responseBody);
        } catch (NoSuchEntryException e) {
            response.setStatus(Status.NOT_FOUND);
        }
    }

    public void onCreate(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        if (!(request.getBody() instanceof JsonObject requestBody))
            throw new BadRequestException("Missing request body");

        if (!(requestBody.get("title") instanceof JsonPrimitive titleInfo))
            throw new BadRequestException("Malformed request body: Malformed title info");
        final String title = titleInfo.getAsString();

        if (!(requestBody.get("group") instanceof JsonPrimitive groupInfo) || !groupInfo.isNumber())
            throw new BadRequestException("Malformed request body: Malformed group info");
        final long group = groupInfo.getAsLong();

        final long id = idGenerator.newId();
        getDatabase().createBoard(id, title, group);

        final JsonObject responseBody = getDatabase().getBoard(id);

        response.setStatus(Status.CREATED);
        response.setBody(responseBody);
    }

    public void onDelete(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long boardId = request.getPathSegmentAsLong(1);

        getDatabase().deleteBoard(boardId);

        response.setStatus(Status.NO_CONTENT);
    }

    public void onEdit(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long boardId = request.getPathSegmentAsLong(1);

        if (!(request.getBody() instanceof JsonObject requestBody))
            throw new BadRequestException("Missing request body");

        final JsonElement titleInfo = requestBody.get("title");
        if (titleInfo != null) {
            if (!(titleInfo instanceof JsonPrimitive json))
                throw new BadRequestException("Malformed request body: Malformed title info");

            final String title = json.getAsString();

            getDatabase().updateBoardTitle(boardId, title);
        }

        final JsonElement groupInfo = requestBody.get("group");
        if (groupInfo != null) {
            if (!(groupInfo instanceof JsonPrimitive json) || !json.isNumber())
                throw new BadRequestException("Malformed request body: Malformed group info");

            final long group = json.getAsLong();

            getDatabase().updateBoardGroup(boardId, group);
        }

        final JsonObject responseBody = getDatabase().getBoard(boardId);

        response.setStatus(Status.OK);
        response.setBody(responseBody);
    }

    private @NotNull Database getDatabase() {
        return this.bruno.getDatabase();
    }
}
