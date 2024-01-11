package org.burrow_studios.obelisk.api.action.entity.board.issue;

import org.burrow_studios.obelisk.api.action.Builder;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.jetbrains.annotations.NotNull;

public interface IssueBuilder extends Builder<Issue> {
    @NotNull IssueBuilder addAssignees(@NotNull User... users);

    @NotNull IssueBuilder setTitle(@NotNull String title) throws IllegalArgumentException;

    @NotNull IssueBuilder setState(@NotNull Issue.State state);

    @NotNull IssueBuilder addTags(@NotNull Tag... tags);
}
