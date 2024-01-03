package org.burrow_studios.obelisk.api.event.entity.board.issue;

import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.jetbrains.annotations.NotNull;

public final class IssueUpdateTitleEvent extends IssueUpdateEvent<String> {
    public IssueUpdateTitleEvent(@NotNull Issue entity, @NotNull String oldValue, @NotNull String newValue) {
        super(entity, oldValue, newValue);
    }
}
