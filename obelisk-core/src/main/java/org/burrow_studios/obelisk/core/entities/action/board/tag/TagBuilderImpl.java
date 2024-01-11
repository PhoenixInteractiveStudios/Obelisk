package org.burrow_studios.obelisk.core.entities.action.board.tag;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.board.tag.TagBuilder;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.BuilderImpl;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.impl.board.BoardImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.TagImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class TagBuilderImpl extends BuilderImpl<Tag> implements TagBuilder {
    public TagBuilderImpl(@NotNull BoardImpl board) {
        super(
                board.getAPI(),
                Tag.class,
                Route.Board.Tag.CREATE.builder().withArg(board.getId()).compile(),
                TagBuilderImpl::build
        );
    }

    protected static @NotNull TagImpl build(@NotNull EntityData data, @NotNull ObeliskImpl api) {
        final JsonObject json = data.toJson();

        final long   id   = json.get("id").getAsLong();
        final String name = json.get("name").getAsString();

        final long boardId = json.get("board").getAsLong();
        final BoardImpl board = api.getBoard(boardId);
        if (board == null)
            throw new IllegalStateException("The board id could not be mapped to a cached board");

        final TagImpl tag = new TagImpl(api, id, board, name);

        board.getAvailableTags().add(tag);
        return tag;
    }

    @Override
    public @NotNull TagBuilderImpl setName(@NotNull String name) {
        data.set("name", new JsonPrimitive(name));
        return this;
    }
}
