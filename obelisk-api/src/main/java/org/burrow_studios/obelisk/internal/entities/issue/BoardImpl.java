package org.burrow_studios.obelisk.internal.entities.issue;

import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.issue.Board;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.entities.TurtleImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class BoardImpl extends TurtleImpl implements Board {
    private @NotNull String title;
    private long groupId;
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

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    @Override
    public long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    @Override
    public @NotNull Group getGroup() {
        // TODO
        return null;
    }

    public void setGroup(@NotNull Group group) {
        this.setGroupId(group.getId());
    }

    @Override
    public @NotNull Set<Long> getAvailableTagIds() {
        return Set.copyOf(this.availableTagIds);
    }

    public @NotNull Set<Long> getAvailableTagIdsMutable() {
        return this.availableTagIds;
    }

    @Override
    public @NotNull Set<TagImpl> getAvailableTags() {
        // TODO
        return Set.of();
    }

    public @NotNull Set<TagImpl> getAvailableTagsMutable() {
        // TODO
        return Set.of();
    }

    @Override
    public @NotNull Set<Long> getIssueIds() {
        return Set.copyOf(this.issueIds);
    }

    public @NotNull Set<Long> getIssueIdsMutable() {
        return this.issueIds;
    }

    @Override
    public @NotNull Set<IssueImpl> getIssues() {
        // TODO
        return Set.of();
    }

    public @NotNull Set<IssueImpl> getIssuesMutable() {
        // TODO
        return Set.of();
    }
}
