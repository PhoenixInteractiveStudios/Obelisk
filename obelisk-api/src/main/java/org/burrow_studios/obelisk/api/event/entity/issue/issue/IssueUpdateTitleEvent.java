package org.burrow_studios.obelisk.api.event.entity.issue.issue;

import org.burrow_studios.obelisk.api.entities.issue.Issue;
import org.jetbrains.annotations.NotNull;

public final class IssueUpdateTitleEvent extends IssueUpdateEvent<String> {
    public IssueUpdateTitleEvent(@NotNull Issue entity, @NotNull String oldValue, @NotNull String newValue) {
        super(entity, oldValue, newValue);
    }
}
