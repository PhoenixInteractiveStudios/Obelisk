package org.burrow_studios.obelisk.api.action.entity.board.issue;

import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.jetbrains.annotations.NotNull;

public interface IssueModifier extends Modifier<Issue> {
    @NotNull IssueModifier addAssignees(@NotNull User... users);

    @NotNull IssueModifier removeAssignees(@NotNull User... users);

    @NotNull IssueModifier setTitle(@NotNull String title);

    @NotNull IssueModifier setState(@NotNull Issue.State state);

    @NotNull IssueModifier addTags(@NotNull Tag... tags);

    @NotNull IssueModifier removeTags(@NotNull Tag... tags);
}