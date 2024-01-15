package org.burrow_studios.obelisk.api.event.entity;

import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.api.event.Event;
import org.burrow_studios.obelisk.api.event.GatewayEvent;
import org.burrow_studios.obelisk.api.event.entity.board.board.BoardCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.board.board.BoardDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.board.issue.IssueCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.board.issue.IssueDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.board.tag.TagCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.board.tag.TagDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.group.GroupCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.group.GroupDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.ticket.TicketCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.ticket.TicketDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.user.UserCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.user.UserDeleteEvent;
import org.jetbrains.annotations.NotNull;

public sealed interface EntityDeleteEvent<E extends Turtle> extends Event, GatewayEvent permits BoardCreateEvent, BoardDeleteEvent, IssueCreateEvent, IssueDeleteEvent, TagCreateEvent, TagDeleteEvent, GroupCreateEvent, GroupDeleteEvent, ProjectCreateEvent, ProjectDeleteEvent, TicketCreateEvent, TicketDeleteEvent, UserCreateEvent, UserDeleteEvent {
    @NotNull E getEntity();
}
