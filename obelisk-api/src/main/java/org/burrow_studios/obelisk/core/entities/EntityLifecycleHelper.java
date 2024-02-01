package org.burrow_studios.obelisk.core.entities;

import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.api.event.GatewayEvent;
import org.burrow_studios.obelisk.api.event.entity.EntityCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
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
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.cache.TurtleCache;
import org.burrow_studios.obelisk.core.entities.impl.GroupImpl;
import org.burrow_studios.obelisk.core.entities.impl.ProjectImpl;
import org.burrow_studios.obelisk.core.entities.impl.TicketImpl;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.BoardImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.IssueImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.TagImpl;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;

public class EntityLifecycleHelper {
    private EntityLifecycleHelper() { }

    public static void createEntity(@NotNull ObeliskImpl api, @NotNull EntityData data, long eventId, @NotNull Class<? extends GatewayEvent> eventType) {
        new CreateHelper(api, eventId, data, eventType)
                .tryCreate(  GroupCreateEvent.class,   GroupImpl::new,   GroupCreateEvent::new)
                .tryCreate(ProjectCreateEvent.class, ProjectImpl::new, ProjectCreateEvent::new)
                .tryCreate( TicketCreateEvent.class,  TicketImpl::new,  TicketCreateEvent::new)
                .tryCreate(   UserCreateEvent.class,    UserImpl::new,    UserCreateEvent::new)
                .tryCreate(  BoardCreateEvent.class,   BoardImpl::new,   BoardCreateEvent::new)
                .tryCreate(  IssueCreateEvent.class,   IssueImpl::new,   IssueCreateEvent::new)
                .tryCreate(    TagCreateEvent.class,     TagImpl::new,     TagCreateEvent::new);
    }

    public static void deleteEntity(@NotNull ObeliskImpl api, long entityId, long eventId, @NotNull Class<? extends GatewayEvent> eventType) {
        new DeleteHelper(api, eventId, entityId, eventType)
                .tryDelete(  GroupDeleteEvent.class, ObeliskImpl::getGroup,     GroupDeleteEvent::new, ObeliskImpl::getGroups)
                .tryDelete(ProjectDeleteEvent.class, ObeliskImpl::getProject, ProjectDeleteEvent::new, ObeliskImpl::getProjects)
                .tryDelete( TicketDeleteEvent.class, ObeliskImpl::getTicket,   TicketDeleteEvent::new, ObeliskImpl::getTickets)
                .tryDelete(   UserDeleteEvent.class, ObeliskImpl::getUser,       UserDeleteEvent::new, ObeliskImpl::getUsers)
                .tryDelete(  BoardDeleteEvent.class, ObeliskImpl::getBoard,     BoardDeleteEvent::new, ObeliskImpl::getBoards)
                .tryDelete(  IssueDeleteEvent.class, ObeliskImpl::getIssue,     IssueDeleteEvent::new, ObeliskImpl::getIssues)
                .tryDelete(    TagDeleteEvent.class, ObeliskImpl::getTag,         TagDeleteEvent::new, ObeliskImpl::getTags);
    }

    private record CreateHelper(
            @NotNull ObeliskImpl api,
            long eventId,
            @NotNull EntityData data,
            @NotNull Class<? extends GatewayEvent> eventType
    ) {
        private <E extends EntityCreateEvent<T>, T extends Turtle> CreateHelper tryCreate(
                @NotNull Class<E> type,
                @NotNull BiFunction<ObeliskImpl, EntityData, T> entityBuilder,
                @NotNull BiFunction<Long, T, E> eventBuilder
        ) {
            if (!type.isAssignableFrom(eventType))
                return this;

            final T entity = entityBuilder.apply(api, data);
            final E event  = eventBuilder.apply(eventId, entity);

            this.api.getEventHandler().handle(event);

            return this;
        }
    }

    private record DeleteHelper(
            @NotNull ObeliskImpl api,
            long eventId,
            long entityId,
            @NotNull Class<? extends GatewayEvent> eventType
    ) {
        private <E extends EntityDeleteEvent<T>, T extends Turtle> DeleteHelper tryDelete(
                @NotNull Class<E> type,
                @NotNull BiFunction<ObeliskImpl, Long, T> entityProvider,
                @NotNull BiFunction<Long, T, E> eventBuilder,
                @NotNull Function<ObeliskImpl, TurtleCache<? extends T>> cacheProvider
        ) {
            if (!type.isAssignableFrom(eventType))
                return this;

            final T entity = entityProvider.apply(api, entityId);
            final E event  = eventBuilder.apply(eventId, entity);

            cacheProvider.apply(api).remove(entity);
            this.api.getEventHandler().handle(event);

            return this;
        }
    }
}
