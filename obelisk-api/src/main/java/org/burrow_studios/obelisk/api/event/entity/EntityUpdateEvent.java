package org.burrow_studios.obelisk.api.event.entity;

import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.api.event.Event;
import org.burrow_studios.obelisk.api.event.GatewayEvent;
import org.burrow_studios.obelisk.api.event.entity.board.board.BoardUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.board.issue.IssueUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.board.tag.TagUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.group.GroupUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.ticket.TicketUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.user.UserUpdateEvent;
import org.jetbrains.annotations.NotNull;

public sealed interface EntityUpdateEvent<E extends Turtle, T> extends Event, GatewayEvent permits BoardUpdateEvent, IssueUpdateEvent, TagUpdateEvent, GroupUpdateEvent, ProjectUpdateEvent, TicketUpdateEvent, UserUpdateEvent {
    @NotNull E getEntity();

    T getOldValue();

    T getNewValue();
}
