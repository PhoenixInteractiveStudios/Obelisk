package org.burrow_studios.obelisk.api.action.entity.board.issue;

import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.jetbrains.annotations.NotNull;

public interface IssueModifier extends Modifier<Issue> {
    @NotNull IssueModifier setTitle(@NotNull String title);

    @NotNull IssueModifier setState(@NotNull Issue.State state);
}
