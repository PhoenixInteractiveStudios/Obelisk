package org.burrow_studios.obelisk.api.event.entity.board.issue;

import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.api.event.entity.AbstractEntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract sealed class IssueEvent extends AbstractEntityEvent<Issue> permits IssueCreateEvent, IssueDeleteEvent, IssueUpdateEvent {
    protected IssueEvent(long id, @NotNull Issue entity) {
        super(id, entity);
    }
}
