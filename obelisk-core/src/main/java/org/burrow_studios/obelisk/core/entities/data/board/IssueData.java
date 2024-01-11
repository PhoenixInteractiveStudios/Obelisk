package org.burrow_studios.obelisk.core.entities.data.board;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.cache.DelegatingTurtleCacheView;
import org.burrow_studios.obelisk.core.cache.TurtleCache;
import org.burrow_studios.obelisk.core.entities.data.Data;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.BoardImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.IssueImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.TagImpl;
import org.jetbrains.annotations.NotNull;

import static org.burrow_studios.obelisk.core.entities.BuildHelper.*;
import static org.burrow_studios.obelisk.core.entities.UpdateHelper.*;

public final class IssueData extends Data<IssueImpl> {
    public IssueData() {
        super();
    }

    public IssueData(long id) {
        super(id);
    }

    public IssueData(@NotNull JsonObject json) {
        super(json);
    }

    @Override
    public @NotNull IssueImpl build(@NotNull ObeliskImpl api) {
        final JsonObject json = toJson();

        final long id = json.get("id").getAsLong();

        final long boardId = json.get("board").getAsLong();
        final BoardImpl board = api.getBoard(boardId);
        if (board == null)
            throw new IllegalStateException("The board id could not be mapped to a cached board");

        final long authorId = json.get("author").getAsLong();
        final UserImpl author = api.getUser(authorId);
        if (author == null)
            throw new IllegalStateException("The author id could not be mapped to a cached user");

        final String title    = json.get("title").getAsString();
        final String stateStr = json.get("state").getAsString();

        final DelegatingTurtleCacheView<UserImpl> assignees = buildDelegatingCacheView(json, "assignees", api.getUsers(), UserImpl.class);
        final Issue.State state = Issue.State.valueOf(stateStr);
        final TurtleCache<TagImpl> availableTags = api.getTags();
        final DelegatingTurtleCacheView<TagImpl> tags = buildDelegatingCacheView(json, "tags", availableTags, TagImpl.class);

        final IssueImpl issue = new IssueImpl(api, id, board, author, assignees, title, state, tags);

        board.getIssues().add(issue);
        return issue;
    }

    @Override
    public void update(@NotNull IssueImpl issue) {
        final JsonObject json = toJson();

        handleUpdateTurtles(json, "assignees", issue::getAssignees);
        handleUpdate(json, "title", JsonElement::getAsString, issue::setTitle);
        handleUpdateEnum(json, "state", Issue.State.class, issue::setState);
        handleUpdateTurtles(json, "tags", issue::getTags);
    }

    public void addAssignees(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.addToArray("assignees", arr);
    }

    public void removeAssignees(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.removeFromArray("assignees", arr);
    }

    public void setTitle(@NotNull String title) {
        this.set("title", new JsonPrimitive(title));
    }

    public void setState(@NotNull Issue.State state) {
        this.set("state", new JsonPrimitive(state.name()));
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
}
