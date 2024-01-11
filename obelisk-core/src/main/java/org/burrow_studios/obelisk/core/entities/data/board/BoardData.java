package org.burrow_studios.obelisk.core.entities.data.board;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.cache.DelegatingTurtleCacheView;
import org.burrow_studios.obelisk.core.entities.data.Data;
import org.burrow_studios.obelisk.core.entities.impl.GroupImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.BoardImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.IssueImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.TagImpl;
import org.jetbrains.annotations.NotNull;

import static org.burrow_studios.obelisk.core.entities.BuildHelper.*;
import static org.burrow_studios.obelisk.core.entities.UpdateHelper.*;

public final class BoardData extends Data<BoardImpl> {
    public BoardData() {
        super();
    }

    public BoardData(long id) {
        super(id);
    }

    public BoardData(@NotNull JsonObject json) {
        super(json);
    }

    @Override
    public @NotNull BoardImpl build(@NotNull ObeliskImpl api) {
        final JsonObject json = toJson();

        final long   id      = json.get("id").getAsLong();
        final String title   = json.get("title").getAsString();

        final long groupId = json.get("group").getAsLong();
        final GroupImpl group = api.getGroup(groupId);
        if (group == null)
            throw new IllegalStateException("The group id could not be mapped to a cached group");

        final DelegatingTurtleCacheView<TagImpl> availableTags = buildDelegatingCacheView(json, "tags", api.getTags(), TagImpl.class);
        final DelegatingTurtleCacheView<IssueImpl> issues = buildDelegatingCacheView(json, "issues", api.getIssues(), IssueImpl.class);

        final BoardImpl board = new BoardImpl(api, id, title, group, availableTags, issues);

        api.getBoards().add(board);
        return board;
    }

    @Override
    public void update(@NotNull BoardImpl board) {
        final JsonObject json = toJson();

        handleUpdate(json, "title", JsonElement::getAsString, board::setTitle);
        handleUpdateTurtle(json, "group", board.getAPI(), ObeliskImpl::getGroup, board::setGroup);
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
