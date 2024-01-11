package org.burrow_studios.obelisk.core.entities.data.board;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.entities.data.Data;
import org.burrow_studios.obelisk.core.entities.impl.board.BoardImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.TagImpl;
import org.jetbrains.annotations.NotNull;

public final class TagData extends Data<TagImpl> {
    public TagData() {
        super();
    }

    public TagData(long id) {
        super(id);
    }

    public TagData(@NotNull JsonObject json) {
        super(json);
    }

    @Override
    public @NotNull TagImpl build(@NotNull ObeliskImpl api) {
        final JsonObject json = toJson();

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
    public void update(@NotNull TagImpl tag) {
        final JsonObject json = toJson();

        handleUpdate(json, "name", JsonElement::getAsString, tag::setName);
    }

    public void setName(@NotNull String name) {
        this.set("name", new JsonPrimitive(name));
    }
}
