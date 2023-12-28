package org.burrow_studios.obelisk.internal.entities.issue;

import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.issue.Board;
import org.burrow_studios.obelisk.api.entities.issue.Issue;
import org.burrow_studios.obelisk.api.entities.issue.Tag;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.entities.TurtleImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class BoardImpl extends TurtleImpl implements Board {
    private final @NotNull String title;
    private final long groupId;
    private final @NotNull Set<Long> availableTagIds;
    private final @NotNull Set<Long> issueIds;

    public BoardImpl(
            @NotNull ObeliskImpl api,
            long id,
            @NotNull String title,
            long groupId,
            @NotNull Set<Long> availableTagIds,
            @NotNull Set<Long> issueIds
    ) {
        super(api, id);
        this.title = title;
        this.groupId = groupId;
        this.availableTagIds = availableTagIds;
        this.issueIds = issueIds;
    }

    @Override
    public @NotNull String getTitle() {
        return this.title;
    }

    @Override
    public long getGroupId() {
        return this.groupId;
    }

    @Override
    public @NotNull Group getGroup() {
        // TODO
        return null;
    }

    @Override
    public @NotNull Set<Long> getAvailableTagIds() {
        return Set.copyOf(this.availableTagIds);
    }

    @Override
    public @NotNull Set<Tag> getAvailableTags() {
        // TODO
        return Set.of();
    }

    @Override
    public @NotNull Set<Long> getIssueIds() {
        return Set.copyOf(this.issueIds);
    }

    @Override
    public @NotNull Set<Issue> getIssues() {
        // TODO
        return Set.of();
    }
}
