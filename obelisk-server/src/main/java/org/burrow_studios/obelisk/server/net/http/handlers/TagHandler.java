package org.burrow_studios.obelisk.server.net.http.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.entities.action.board.tag.TagBuilderImpl;
import org.burrow_studios.obelisk.core.entities.action.board.tag.TagModifierImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.BoardImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.TagImpl;
import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.burrow_studios.obelisk.server.net.http.Request;
import org.burrow_studios.obelisk.server.net.http.ResponseBuilder;
import org.burrow_studios.obelisk.server.net.http.exceptions.APIException;
import org.burrow_studios.obelisk.server.net.http.exceptions.BadRequestException;
import org.burrow_studios.obelisk.server.net.http.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.server.net.http.exceptions.NotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TagHandler {
    private final @NotNull NetworkHandler networkHandler;

    public TagHandler(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void onGetAll(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        JsonArray json = new JsonArray();
        for (long id : getAPI().getTags().getIdsAsImmutaleSet())
            json.add(id);

        response.setCode(200);
        response.setBody(json);
    }

    public void onGet(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long tagId = request.getSegmentLong(3);

        final TagImpl issue = getAPI().getTag(tagId);
        if (issue == null)
            throw new NotFoundException();

        response.setCode(200);
        response.setBody(issue.toJson());
    }

    public void onCreate(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        Optional<JsonObject> body = request.optionalBody()
                .map(JsonElement::getAsJsonObject);

        final long boardId = request.getSegmentLong(1);
        final BoardImpl board = getAPI().getBoard(boardId);
        if (board == null)
            throw new NotFoundException();

        if (body.isEmpty())
            throw new BadRequestException("No content");

        final JsonObject json = body.get();
        final JsonObject result;

        final TagBuilderImpl builder;

        try {
            final String name = json.get("name").getAsString();

            builder = board.createTag()
                    .setName(name);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Malformed body");
        }

        try {
            result = ((TagImpl) builder.await()).toJson();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(result);
    }

    public void onDelete(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long tagId = request.getSegmentLong(3);
        final TagImpl tag = getAPI().getTag(tagId);

        if (tag != null) {
            try {
                tag.delete().await();
            } catch (Exception e) {
                throw new InternalServerErrorException();
            }
        }

        response.setCode(204);
    }

    public void onEdit(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long tagId = request.getSegmentLong(3);

        Optional<JsonObject> body = request.optionalBody()
                .map(JsonElement::getAsJsonObject);

        if (body.isEmpty())
            throw new BadRequestException("No content");

        final JsonObject json = body.get();

        TagImpl tag = getAPI().getTag(tagId);
        if (tag == null)
            throw new NotFoundException();
        final TagModifierImpl modifier = tag.modify();

        try {
            Optional.ofNullable(json.get("name"))
                    .map(JsonElement::getAsString)
                    .ifPresent(modifier::setName);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Malformed body");
        }

        try {
            tag = ((TagImpl) modifier.await());
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(tag.toJson());
    }

    private @NotNull ObeliskImpl getAPI() {
        return this.networkHandler.getServer().getAPI();
    }
}
