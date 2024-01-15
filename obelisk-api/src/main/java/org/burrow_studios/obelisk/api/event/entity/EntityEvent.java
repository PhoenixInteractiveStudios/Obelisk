package org.burrow_studios.obelisk.api.event.entity;

import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.api.event.AbstractEvent;
import org.burrow_studios.obelisk.api.event.entity.board.board.BoardEvent;
import org.burrow_studios.obelisk.api.event.entity.board.issue.IssueEvent;
import org.burrow_studios.obelisk.api.event.entity.board.tag.TagEvent;
import org.burrow_studios.obelisk.api.event.entity.group.GroupEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectEvent;
import org.burrow_studios.obelisk.api.event.entity.ticket.TicketEvent;
import org.burrow_studios.obelisk.api.event.entity.user.UserEvent;
import org.jetbrains.annotations.NotNull;

public abstract sealed class EntityEvent<E extends Turtle> extends AbstractEvent permits BoardEvent, IssueEvent, TagEvent, GroupEvent, ProjectEvent, TicketEvent, UserEvent {
    protected final @NotNull E entity;

    protected EntityEvent(long id, @NotNull E entity) {
        super(entity.getAPI(), id);
        this.entity = entity;
    }

    public final @NotNull E getEntity() {
        return this.entity;
    }
}
