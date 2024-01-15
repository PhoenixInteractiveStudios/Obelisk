package org.burrow_studios.obelisk.api.event.entity;

import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.api.event.UpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.board.board.BoardUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.board.issue.IssueUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.board.tag.TagUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.group.GroupUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.ticket.TicketUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.user.UserUpdateEvent;

public sealed interface EntityUpdateEvent<E extends Turtle, T> extends EntityEvent<E>, UpdateEvent<T> permits BoardUpdateEvent, IssueUpdateEvent, TagUpdateEvent, GroupUpdateEvent, ProjectUpdateEvent, TicketUpdateEvent, UserUpdateEvent {

}
