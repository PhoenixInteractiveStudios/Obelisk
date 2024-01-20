package org.burrow_studios.obelisk.server.db.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.impl.board.TagImpl;
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

public class TagHandler implements RequestHandler {
    private final TurtleGenerator turtleGenerator;
    private final EntityProvider provider;

    public TagHandler(@NotNull EntityProvider provider) {
        this.provider = provider;
        this.turtleGenerator = TurtleGenerator.get(provider.getClass().getSimpleName());
    }

    public @NotNull Response onGetAll(@NotNull Request request) {
        final long boardId = (long) request.getEndpoint().args()[1];

        final JsonArray tags = new JsonArray();
        final Set<Long> tagIds;

        try {
            tagIds = this.getDB().getTagIds(boardId);
        } catch (DatabaseException e) {
            return request.respond(500, null);
        }

        for (long tagId : tagIds)
            tags.add(tagId);

        return request.respond(200, tags);
    }

    public @NotNull Response onGet(@NotNull Request request) {
        final long boardId = (long) request.getEndpoint().args()[1];
        final long   tagId = (long) request.getEndpoint().args()[3];
        final JsonObject tag;

        try {
            tag = this.getDB().getTag(boardId, tagId);
        } catch (NoSuchEntryException e) {
            return request.respond(404, null);
        } catch (DatabaseException e) {
            return request.respond(500, null);
        }

        return request.respond(200, tag);
    }

    public @NotNull Response onCreate(@NotNull Request request) {
        final long boardId = (long) request.getEndpoint().args()[1];

        final long id = this.turtleGenerator.newId();

        final JsonObject json = request.optionalContent()
                .map(JsonElement::getAsJsonObject)
                .orElse(null);

        if (json == null)
            return request.respond(400, null);

        json.addProperty("id", id);
        json.addProperty("board", boardId);

        final TagImpl tag = new TagImpl(provider.getAPI(), new EntityData(json));

        this.getDB().createTag(
                tag.getBoard().getId(),
                tag.getId(),
                tag.getName()
        );

        return request.respond(200, tag.toJson());
    }

    public @NotNull Response onDelete(@NotNull Request request) {
        final long boardId = (long) request.getEndpoint().args()[1];
        final long   tagId = (long) request.getEndpoint().args()[3];

        this.getDB().deleteTag(boardId, tagId);

        return request.respond(204, null);
    }

    public @NotNull Response onEdit(@NotNull Request request) {
        final long boardId = (long) request.getEndpoint().args()[1];
        final long   tagId = (long) request.getEndpoint().args()[3];
        final JsonObject json = request.optionalContent()
                .map(JsonElement::getAsJsonObject)
                .orElse(null);

        if (json == null)
            return request.respond(400, null);

        Optional.ofNullable(json.get("title"))
                .map(JsonElement::getAsString)
                .ifPresent(title -> getDB().updateTagTitle(boardId, tagId, title));

        final JsonObject tag = getDB().getTag(boardId, tagId);
        return request.respond(200, tag);
    }

    private @NotNull BoardDB getDB() {
        return this.provider.getBoardDB();
    }
}
