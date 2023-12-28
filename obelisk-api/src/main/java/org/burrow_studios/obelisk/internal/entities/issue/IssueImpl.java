package org.burrow_studios.obelisk.internal.entities.issue;

import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.entities.issue.Board;
import org.burrow_studios.obelisk.api.entities.issue.Issue;
import org.burrow_studios.obelisk.api.entities.issue.Tag;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.entities.TurtleImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class IssueImpl extends TurtleImpl implements Issue {
    private final long boardId;
    private final long authorId;
    private final @NotNull Set<Long> assigneeIds;
    private final @NotNull String title;
    private final @NotNull State state;
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

    @Override
    public @NotNull Set<User> getAssignees() {
        // TODO
        return null;
    }

    @Override
    public @NotNull String getTitle() {
        return this.title;
    }

    @Override
    public @NotNull State getState() {
        return this.state;
    }

    @Override
    public @NotNull Set<Long> getTagIds() {
        return Set.copyOf(this.tagIds);
    }

    @Override
    public @NotNull Set<Tag> getTags() {
        // TODO
        return null;
    }
}
