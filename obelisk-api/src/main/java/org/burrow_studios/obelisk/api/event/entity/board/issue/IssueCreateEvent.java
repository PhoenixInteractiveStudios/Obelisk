package org.burrow_studios.obelisk.api.event.entity.board.issue;

import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.api.event.entity.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public final class IssueCreateEvent extends IssueEvent implements EntityCreateEvent<Issue> {
    public IssueCreateEvent(long id, @NotNull Issue entity) {
        super(id, entity);
    }
}
