package org.burrow_studios.obelisk.internal.entities.issue;

import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.entities.issue.Board;
import org.burrow_studios.obelisk.api.entities.issue.Issue;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.cache.DelegatingTurtleCacheView;
import org.burrow_studios.obelisk.internal.entities.TurtleImpl;
import org.burrow_studios.obelisk.internal.entities.UserImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class IssueImpl extends TurtleImpl implements Issue {
    private final long boardId;
    private final long authorId;
    private final @NotNull DelegatingTurtleCacheView<UserImpl> assignees;
    private @NotNull String title;
    private @NotNull State state;
    private final @NotNull DelegatingTurtleCacheView<TagImpl> tags;

    public IssueImpl(
            @NotNull ObeliskImpl api,
            long id,
            long boardId,
            long authorId,
            @NotNull DelegatingTurtleCacheView<UserImpl> assignees,
            @NotNull String title,
            @NotNull State state,
            @NotNull DelegatingTurtleCacheView<TagImpl> tags
    ) {
        super(api, id);
        this.boardId = boardId;
        this.authorId = authorId;
        this.assignees = assignees;
        this.title = title;
        this.state = state;
        this.tags = tags;
    }

    @Override
    public long getBoardId() {
        return this.boardId;
    }

    @Override
    public @NotNull Board getBoard() {
        // TODO
        return null;
    }

    @Override
    public long getAuthorId() {
        return this.authorId;
    }

    @Override
    public @NotNull User getAuthor() {
        // TODO
        return null;
    }

    @Override
    public @NotNull Set<Long> getAssigneeIds() {
        return this.assignees.getIdsAsImmutaleSet();
    }

    @Override
    public @NotNull TurtleSetView<UserImpl> getAssignees() {
        return this.assignees;
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
    public @NotNull Set<Long> getTagIds() {
        return this.tags.getIdsAsImmutaleSet();
    }

    @Override
    public @NotNull TurtleSetView<TagImpl> getTags() {
        return this.tags;
    }
}
