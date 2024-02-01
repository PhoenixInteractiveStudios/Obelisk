package org.burrow_studios.obelisk.core.entities.impl.board;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.ActionImpl;
import org.burrow_studios.obelisk.core.action.DeleteActionImpl;
import org.burrow_studios.obelisk.core.cache.DelegatingTurtleCacheView;
import org.burrow_studios.obelisk.core.cache.TurtleCache;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.action.board.issue.IssueModifierImpl;
import org.burrow_studios.obelisk.core.entities.impl.TurtleImpl;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.burrow_studios.obelisk.core.net.http.Endpoints;
import org.jetbrains.annotations.NotNull;

import static org.burrow_studios.obelisk.core.entities.BuildHelper.buildDelegatingCacheView;

public final class IssueImpl extends TurtleImpl implements Issue {
    private final @NotNull BoardImpl board;
    private final @NotNull UserImpl author;
    private final @NotNull DelegatingTurtleCacheView<UserImpl> assignees;
    private @NotNull String title;
    private @NotNull State state;
    private final @NotNull DelegatingTurtleCacheView<TagImpl> tags;

    public IssueImpl(
            @NotNull ObeliskImpl api,
            long id,
            @NotNull BoardImpl board,
            @NotNull UserImpl author,
            @NotNull DelegatingTurtleCacheView<UserImpl> assignees,
            @NotNull String title,
            @NotNull State state,
            @NotNull DelegatingTurtleCacheView<TagImpl> tags
    ) {
        super(api, id);
        this.board = board;
        this.author = author;
        this.assignees = assignees;
        this.title = title;
        this.state = state;
        this.tags = tags;
    }

    public IssueImpl(@NotNull ObeliskImpl api, @NotNull EntityData data) {
        super(api, data.getId());

        final JsonObject json = data.toJson();

        final long boardId = json.get("board").getAsLong();
        final BoardImpl board = api.getBoard(boardId);
        if (board == null)
            throw new IllegalStateException("The board id could not be mapped to a cached board");
        this.board = board;

        final long authorId = json.get("author").getAsLong();
        final UserImpl author = api.getUser(authorId);
        if (author == null)
            throw new IllegalStateException("The author id could not be mapped to a cached user");
        this.author = author;

        this.title = json.get("title").getAsString();

        this.assignees = buildDelegatingCacheView(json, "assignees", api.getUsers(), UserImpl.class);

        final String stateStr = json.get("state").getAsString();
        this.state = Issue.State.valueOf(stateStr);

        final TurtleCache<TagImpl> availableTags = api.getTags();
        this.tags = buildDelegatingCacheView(json, "tags", availableTags, TagImpl.class);
    }

    @Override
    public @NotNull JsonObject toJson() {
        JsonObject json = super.toJson();
        json.addProperty("board", board.getId());
        json.addProperty("author", author.getId());

        JsonArray assigneeIds = new JsonArray();
        for (long tagId : this.assignees.getIdsAsImmutaleSet())
            assigneeIds.add(tagId);
        json.add("assignees", assigneeIds);

        json.addProperty("title", title);
        json.addProperty("state", state.name());

        JsonArray tagIds = new JsonArray();
        for (long tagId : this.tags.getIdsAsImmutaleSet())
            tagIds.add(tagId);
        json.add("tags", tagIds);

        return json;
    }

    @Override
    public @NotNull IssueModifierImpl modify() {
        return new IssueModifierImpl(this);
    }

    @Override
    public @NotNull DeleteAction<Issue> delete() {
        return new DeleteActionImpl<>(
                this.getAPI(),
                Issue.class,
                this.getId(),
                Endpoints.Board.Issue.DELETE.builder()
                        .withArg(getBoard().getId())
                        .withArg(getId())
                        .compile()
        );
    }

    @Override
    public @NotNull BoardImpl getBoard() {
        return this.board;
    }

    @Override
    public @NotNull UserImpl getAuthor() {
        return this.author;
    }

    @Override
    public @NotNull DelegatingTurtleCacheView<UserImpl> getAssignees() {
        return this.assignees;
    }

    @Override
    public @NotNull ActionImpl<Issue> addAssignee(@NotNull User user) {
        return new ActionImpl<>(this.api, this,
                Endpoints.Board.Issue.ADD_ASSIGNEE.builder()
                        .withArg(getBoard().getId())
                        .withArg(getId())
                        .withArg(user.getId())
                        .compile()
        );
    }

    @Override
    public @NotNull ActionImpl<Issue> removeAssignee(@NotNull User user) {
        return new ActionImpl<>(this.api, this,
                Endpoints.Board.Issue.DEL_ASSIGNEE.builder()
                        .withArg(getBoard().getId())
                        .withArg(getId())
                        .withArg(user.getId())
                        .compile()
        );
    }

    @Override
    public @NotNull String getTitle() {
        return this.title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    @Override
    public @NotNull State getState() {
        return this.state;
    }

    public void setState(@NotNull State state) {
        this.state = state;
    }

    @Override
    public @NotNull DelegatingTurtleCacheView<TagImpl> getTags() {
        return this.tags;
    }

    @Override
    public @NotNull ActionImpl<Issue> addTag(@NotNull Tag tag) {
        return new ActionImpl<>(this.api, this,
                Endpoints.Board.Issue.ADD_TAG.builder()
                        .withArg(getBoard().getId())
                        .withArg(getId())
                        .withArg(tag.getId())
                        .compile()
        );
    }

    @Override
    public @NotNull ActionImpl<Issue> removeTag(@NotNull Tag tag) {
        return new ActionImpl<>(this.api, this,
                Endpoints.Board.Issue.DEL_TAG.builder()
                        .withArg(getBoard().getId())
                        .withArg(getId())
                        .withArg(tag.getId())
                        .compile()
        );
    }
}
