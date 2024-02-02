package org.burrow_studios.obelisk.server.net.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.http.server.Request;
import org.burrow_studios.obelisk.commons.http.server.ResponseBuilder;
import org.burrow_studios.obelisk.commons.http.server.exceptions.BadRequestException;
import org.burrow_studios.obelisk.commons.http.server.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.commons.http.server.exceptions.NotFoundException;
import org.burrow_studios.obelisk.commons.http.server.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.entities.action.board.BoardBuilderImpl;
import org.burrow_studios.obelisk.core.entities.action.board.BoardModifierImpl;
import org.burrow_studios.obelisk.core.entities.impl.GroupImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.BoardImpl;
import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BoardHandler {
    private final @NotNull NetworkHandler networkHandler;

    public BoardHandler(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void onGetAll(@NotNull Request request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        JsonArray json = new JsonArray();
        for (long id : getAPI().getBoards().getIdsAsImmutaleSet())
            json.add(id);

        response.setCode(200);
        response.setBody(json);
    }

    public void onGet(@NotNull Request request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        final long boardId = request.getSegmentLong(1);

        final BoardImpl board = getAPI().getBoard(boardId);
        if (board == null)
            throw new NotFoundException();

        response.setCode(200);
        response.setBody(board.toJson());
    }

    public void onCreate(@NotNull Request request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        Optional<JsonObject> body = request.optionalBody()
                .map(JsonElement::getAsJsonObject);

        if (body.isEmpty())
            throw new BadRequestException("No content");

        final JsonObject json = body.get();
        final JsonObject result;

        final BoardBuilderImpl builder;

        try {
            final String title   = json.get("title").getAsString();
            final long   groupId = json.get("group").getAsLong();

            GroupImpl group = getAPI().getGroup(groupId);
            if (group == null)
                throw new IllegalArgumentException("Invalid group id: " + groupId);

            builder = getAPI().createBoard()
                    .setTitle(title)
                    .setGroup(group);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Malformed body");
        }

        try {
            result = ((BoardImpl) builder.await()).toJson();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(result);
    }

    public void onDelete(@NotNull Request request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        final long boardId = request.getSegmentLong(1);
        final BoardImpl board = getAPI().getBoard(boardId);

        if (board != null) {
            try {
                board.delete().await();
            } catch (Exception e) {
                throw new InternalServerErrorException();
            }
        }

        response.setCode(204);
    }

    public void onEdit(@NotNull Request request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        final long boardId = request.getSegmentLong(1);

        Optional<JsonObject> body = request.optionalBody()
                .map(JsonElement::getAsJsonObject);

        if (body.isEmpty())
            throw new BadRequestException("No content");

        final JsonObject json = body.get();

        BoardImpl board = getAPI().getBoard(boardId);
        if (board == null)
            throw new NotFoundException();
        final BoardModifierImpl modifier = board.modify();

        try {
            Optional.ofNullable(json.get("title"))
                    .map(JsonElement::getAsString)
                    .ifPresent(modifier::setTitle);

            Optional.ofNullable(json.get("group"))
                    .map(JsonElement::getAsLong)
                    .ifPresent(groupId -> {
                        GroupImpl group = getAPI().getGroup(groupId);
                        if (group == null)
                            throw new IllegalArgumentException("Invalid group id: " + groupId);
                        modifier.setGroup(group);
                    });
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Malformed body");
        }

        try {
            board = ((BoardImpl) modifier.await());
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(board.toJson());
    }

    private @NotNull ObeliskImpl getAPI() {
        return this.networkHandler.getServer().getAPI();
    }
}
