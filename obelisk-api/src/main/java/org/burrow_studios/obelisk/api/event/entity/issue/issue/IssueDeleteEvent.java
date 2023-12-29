package org.burrow_studios.obelisk.api.event.entity.issue.issue;

import org.burrow_studios.obelisk.api.entities.issue.Issue;
import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public final class IssueDeleteEvent extends IssueEvent implements EntityDeleteEvent<Issue> {
    public IssueDeleteEvent(@NotNull Issue entity) {
        super(entity);
    }
}
