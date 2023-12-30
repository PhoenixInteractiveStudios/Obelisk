package org.burrow_studios.obelisk.api.event.entity.issue.issue;

import org.burrow_studios.obelisk.api.entities.issue.Issue;
import org.burrow_studios.obelisk.api.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class IssueEvent extends EntityEvent<Issue> {
    protected IssueEvent(@NotNull Issue entity) {
        super(entity);
    }
}
