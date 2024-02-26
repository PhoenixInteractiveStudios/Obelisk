package org.burrow_studios.obelisk.bruno.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.bruno.Bruno;
import org.burrow_studios.obelisk.bruno.database.Database;
import org.burrow_studios.obelisk.bruno.exceptions.NoSuchEntryException;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.Status;
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
        final String title = request.bodyHelper().requireElementAsString("title");
        final long   group = request.bodyHelper().requireElementAsLong("group");

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

        request.bodyHelper().optionalElementAsString("title").ifPresent(title -> {
            getDatabase().updateBoardTitle(boardId, title);
        });
        request.bodyHelper().optionalElementAsLong("group").ifPresent(group -> {
            getDatabase().updateBoardGroup(boardId, group);
        });

        final JsonObject responseBody = getDatabase().getBoard(boardId);

        response.setStatus(Status.OK);
        response.setBody(responseBody);
    }

    private @NotNull Database getDatabase() {
        return this.bruno.getDatabase();
    }
}
