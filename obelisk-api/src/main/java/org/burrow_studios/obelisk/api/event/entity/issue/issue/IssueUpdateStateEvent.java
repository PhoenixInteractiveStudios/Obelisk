package org.burrow_studios.obelisk.api.event.entity.issue.issue;

import org.burrow_studios.obelisk.api.entities.issue.Issue;
import org.jetbrains.annotations.NotNull;

public final class IssueUpdateStateEvent extends IssueUpdateEvent<Issue.State> {
    public IssueUpdateStateEvent(@NotNull Issue entity, @NotNull Issue.State oldValue, @NotNull Issue.State newValue) {
        super(entity, oldValue, newValue);
    }
}
