package org.burrow_studios.obelisk.internal.data.board;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.internal.EntityBuilder;
import org.burrow_studios.obelisk.internal.data.Data;
import org.burrow_studios.obelisk.internal.entities.board.BoardImpl;
import org.jetbrains.annotations.NotNull;

public final class BoardData extends Data<Board, BoardImpl> {
    public BoardData() {
        super();
    }

    public BoardData(long id) {
        super(id);
    }

    @Override
    public @NotNull BoardImpl build(@NotNull EntityBuilder builder) {
        return builder.buildBoard(toJson());
    }

    @Override
    public void update(@NotNull BoardImpl board) {
        final JsonObject json = toJson();

        handleUpdate(json, "title", JsonElement::getAsString, board::setTitle);
        handleUpdate(json, "group", JsonElement::getAsLong, board::setGroupId);
        handleUpdateTurtles(json, "tags", board::getAvailableTags);
        handleUpdateTurtles(json, "issues", board::getIssues);
    }

    public void setTitle(@NotNull String title) {
        this.set("title", new JsonPrimitive(title));
    }

    public void setGroup(@NotNull Group group) {
        this.set("group", new JsonPrimitive(group.getId()));
    }

    public void addTags(@NotNull Tag... tags) {
        JsonArray arr = new JsonArray();
        for (Tag tag : tags)
            arr.add(tag.getId());
        this.addToArray("tags", arr);
    }

    public void removeTags(@NotNull Tag... tags) {
        JsonArray arr = new JsonArray();
        for (Tag tag : tags)
            arr.add(tag.getId());
        this.removeFromArray("tags", arr);
    }

    public void addIssues(@NotNull Issue... issues) {
        JsonArray arr = new JsonArray();
        for (Issue issue : issues)
            arr.add(issue.getId());
        this.addToArray("issues", arr);
    }

    public void removeIssues(@NotNull Issue... issues) {
        JsonArray arr = new JsonArray();
        for (Issue issue : issues)
            arr.add(issue.getId());
        this.removeFromArray("issues", arr);
    }
}
