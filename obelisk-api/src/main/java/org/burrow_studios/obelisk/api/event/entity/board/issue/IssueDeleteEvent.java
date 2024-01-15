package org.burrow_studios.obelisk.api.event.entity.board.issue;

import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public final class IssueDeleteEvent extends IssueEvent implements EntityDeleteEvent<Issue> {
    public IssueDeleteEvent(long id, @NotNull Issue entity) {
        super(id, entity);
    }

    @Override
    public int getOpcode() {
        return 151;
    }
}
