package org.burrow_studios.obelisk.server.db.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.impl.board.BoardImpl;
import org.burrow_studios.obelisk.core.source.Request;
import org.burrow_studios.obelisk.core.source.Response;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.EntityProvider;
import org.burrow_studios.obelisk.server.db.NoSuchEntryException;
import org.burrow_studios.obelisk.server.db.RequestHandler;
import org.burrow_studios.obelisk.server.db.entity.BoardDB;
import org.burrow_studios.obelisk.util.TurtleGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public class BoardHandler implements RequestHandler {
    private final TurtleGenerator turtleGenerator;
    private final EntityProvider provider;

    public BoardHandler(@NotNull EntityProvider provider) {
        this.provider = provider;
        this.turtleGenerator = TurtleGenerator.get(provider.getClass().getSimpleName());
    }

    public @NotNull Response onGetAll(@NotNull Request request) {
        final JsonArray boards = new JsonArray();
        final Set<Long> boardIds;

        try {
            boardIds = this.getDB().getBoardIds();
        } catch (DatabaseException e) {
            return request.respond(500, null);
        }

        for (long boardId : boardIds)
            boards.add(boardId);

        return request.respond(200, boards);
    }

    public @NotNull Response onGet(@NotNull Request request) {
        final long boardId = (long) request.getEndpoint().args()[1];
        final JsonObject board;

        try {
            board = this.getDB().getBoard(boardId);
        } catch (NoSuchEntryException e) {
            return request.respond(404, null);
        } catch (DatabaseException e) {
            return request.respond(500, null);
        }

        return request.respond(200, board);
    }

    public @NotNull Response onCreate(@NotNull Request request) {
        final long id = this.turtleGenerator.newId();

        final JsonObject json = request.optionalContent()
                .map(JsonElement::getAsJsonObject)
                .orElse(null);

        if (json == null)
            return request.respond(400, null);

        json.addProperty("id", id);

        final BoardImpl board = new BoardImpl(provider.getAPI(), new EntityData(json));

        this.getDB().createBoard(
                board.getId(),
                board.getTitle(),
                board.getGroup().getId()
        );

        return request.respond(200, board.toJson());
    }

    public @NotNull Response onDelete(@NotNull Request request) {
        final long boardId = (long) request.getEndpoint().args()[1];

        this.getDB().deleteBoard(boardId);

        return request.respond(204, null);
    }

    public @NotNull Response onEdit(@NotNull Request request) {
        final long boardId = (long) request.getEndpoint().args()[1];
        final JsonObject json = request.optionalContent()
                .map(JsonElement::getAsJsonObject)
                .orElse(null);

        if (json == null)
            return request.respond(400, null);

        Optional.ofNullable(json.get("title"))
                .map(JsonElement::getAsString)
                .ifPresent(title -> getDB().updateBoardTitle(boardId, title));

        Optional.ofNullable(json.get("group"))
                .map(JsonElement::getAsLong)
                .ifPresent(groupId -> getDB().updateBoardGroup(boardId, groupId));

        final JsonObject board = getDB().getBoard(boardId);
        return request.respond(200, board);
    }

    private @NotNull BoardDB getDB() {
        return this.provider.getBoardDB();
    }
}
