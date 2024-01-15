package org.burrow_studios.obelisk.api.event.entity;

import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.api.event.AbstractEvent;
import org.jetbrains.annotations.NotNull;

public abstract sealed class EntityEvent<E extends Turtle> extends AbstractEvent permits org.burrow_studios.obelisk.api.event.entity.board.board.BoardEvent, org.burrow_studios.obelisk.api.event.entity.board.issue.IssueEvent, org.burrow_studios.obelisk.api.event.entity.board.tag.TagEvent, org.burrow_studios.obelisk.api.event.entity.group.GroupEvent, org.burrow_studios.obelisk.api.event.entity.project.ProjectEvent, org.burrow_studios.obelisk.api.event.entity.ticket.TicketEvent, org.burrow_studios.obelisk.api.event.entity.user.UserEvent {
    protected final @NotNull E entity;

    protected EntityEvent(long id, @NotNull E entity) {
        super(entity.getAPI(), id);
        this.entity = entity;
    }

    public final @NotNull E getEntity() {
        return this.entity;
    }
}
