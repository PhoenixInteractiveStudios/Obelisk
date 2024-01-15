package org.burrow_studios.obelisk.api.event.entity;

import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.api.event.entity.board.board.BoardCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.board.issue.IssueCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.board.tag.TagCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.group.GroupCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.ticket.TicketCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.user.UserCreateEvent;

public sealed interface EntityCreateEvent<E extends Turtle> extends EntityEvent<E> permits BoardCreateEvent, IssueCreateEvent, TagCreateEvent, GroupCreateEvent, ProjectCreateEvent, TicketCreateEvent, UserCreateEvent {

}
