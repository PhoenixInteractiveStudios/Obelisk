package org.burrow_studios.obelisk.core.entities.action.board.issue;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.board.issue.IssueBuilder;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.BuilderImpl;
import org.burrow_studios.obelisk.core.cache.DelegatingTurtleCacheView;
import org.burrow_studios.obelisk.core.cache.TurtleCache;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.checks.board.IssueChecks;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.BoardImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.IssueImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.TagImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

import static org.burrow_studios.obelisk.core.entities.BuildHelper.buildDelegatingCacheView;

public class IssueBuilderImpl extends BuilderImpl<Issue> implements IssueBuilder {
    public IssueBuilderImpl(@NotNull BoardImpl board) {
        super(
                board.getAPI(),
                Issue.class,
                Route.Board.Issue.CREATE.builder().withArg(board.getId()).compile(),
                IssueBuilderImpl::build
        );
    }

    protected static @NotNull IssueImpl build(@NotNull EntityData data, @NotNull ObeliskImpl api) {
        final JsonObject json = data.toJson();

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
    public @NotNull IssueBuilderImpl addAssignees(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        data.addToArray("assignees", arr);
        return this;
    }

    @Override
    public @NotNull IssueBuilderImpl setTitle(@NotNull String title) throws IllegalArgumentException {
        IssueChecks.checkTitle(title);
        data.set("title", new JsonPrimitive(title));
        return this;
    }

    @Override
    public @NotNull IssueBuilderImpl setState(@NotNull Issue.State state) {
        data.set("state", new JsonPrimitive(state.name()));
        return this;
    }

    @Override
    public @NotNull IssueBuilderImpl addTags(@NotNull Tag... tags) {
        JsonArray arr = new JsonArray();
        for (Tag tag : tags)
            arr.add(tag.getId());
        data.addToArray("tags", arr);
        return this;
    }
}
