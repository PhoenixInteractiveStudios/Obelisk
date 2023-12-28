package org.burrow_studios.obelisk.internal.entities.issue;

import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.entities.issue.Board;
import org.burrow_studios.obelisk.api.entities.issue.Issue;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.entities.TurtleImpl;
import org.burrow_studios.obelisk.internal.entities.UserImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class IssueImpl extends TurtleImpl implements Issue {
    private final long boardId;
    private final long authorId;
    private final @NotNull Set<Long> assigneeIds;
    private @NotNull String title;
    private @NotNull State state;
    private final @NotNull Set<Long> tagIds;

    public IssueImpl(
            @NotNull ObeliskImpl api,
            long id,
            long boardId,
            long authorId,
            @NotNull Set<Long> assigneeIds,
            @NotNull String title,
            @NotNull State state,
            @NotNull Set<Long> tagIds
    ) {
        super(api, id);
        this.boardId = boardId;
        this.authorId = authorId;
        this.assigneeIds = assigneeIds;
        this.title = title;
        this.state = state;
        this.tagIds = tagIds;
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
        return Set.copyOf(this.assigneeIds);
    }

    public @NotNull Set<Long> getAssigneeIdsMutable() {
        return this.assigneeIds;
    }

    @Override
    public @NotNull Set<UserImpl> getAssignees() {
        // TODO
        return null;
    }

    public @NotNull Set<UserImpl> getAssigneesMutable() {
        // TODO
        return null;
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
        return Set.copyOf(this.tagIds);
    }

    public @NotNull Set<Long> getTagIdsMutable() {
        return this.tagIds;
    }

    @Override
    public @NotNull Set<TagImpl> getTags() {
        // TODO
        return null;
    }

    public @NotNull Set<TagImpl> getTagsMutable() {
        // TODO
        return null;
    }
}
