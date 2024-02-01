package org.burrow_studios.obelisk.core.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.*;
import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.api.event.entity.EntityUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.board.board.BoardUpdateGroupEvent;
import org.burrow_studios.obelisk.api.event.entity.board.board.BoardUpdateTitleEvent;
import org.burrow_studios.obelisk.api.event.entity.board.issue.IssueUpdateAssigneesEvent;
import org.burrow_studios.obelisk.api.event.entity.board.issue.IssueUpdateStateEvent;
import org.burrow_studios.obelisk.api.event.entity.board.issue.IssueUpdateTagsEvent;
import org.burrow_studios.obelisk.api.event.entity.board.issue.IssueUpdateTitleEvent;
import org.burrow_studios.obelisk.api.event.entity.board.tag.TagUpdateNameEvent;
import org.burrow_studios.obelisk.api.event.entity.group.GroupUpdateMembersEvent;
import org.burrow_studios.obelisk.api.event.entity.group.GroupUpdateNameEvent;
import org.burrow_studios.obelisk.api.event.entity.group.GroupUpdatePositionEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectUpdateMembersEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectUpdateStateEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectUpdateTimingsEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectUpdateTitleEvent;
import org.burrow_studios.obelisk.api.event.entity.ticket.TicketUpdateStateEvent;
import org.burrow_studios.obelisk.api.event.entity.ticket.TicketUpdateTagsEvent;
import org.burrow_studios.obelisk.api.event.entity.ticket.TicketUpdateTitleEvent;
import org.burrow_studios.obelisk.api.event.entity.ticket.TicketUpdateUsersEvent;
import org.burrow_studios.obelisk.api.event.entity.user.UserUpdateDiscordIdsEvent;
import org.burrow_studios.obelisk.api.event.entity.user.UserUpdateMinecraftIdsEvent;
import org.burrow_studios.obelisk.api.event.entity.user.UserUpdateNameEvent;
import org.burrow_studios.obelisk.core.cache.DelegatingTurtleCacheView;
import org.burrow_studios.obelisk.core.entities.action.project.ProjectUtils;
import org.burrow_studios.obelisk.core.entities.impl.*;
import org.burrow_studios.obelisk.core.entities.impl.board.BoardImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.IssueImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.TagImpl;
import org.burrow_studios.obelisk.common.function.Function4;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class EntityUpdater {
    private EntityUpdater() { }

    public static void update(@NotNull Turtle turtle, @NotNull EntityData data, @Nullable Long eventId) {
        if (turtle instanceof GroupImpl group)
            updateGroup(data, group, eventId);
        if (turtle instanceof ProjectImpl project)
            updateProject(data, project, eventId);
        if (turtle instanceof TicketImpl ticket)
            updateTicket(data, ticket, eventId);
        if (turtle instanceof UserImpl user)
            updateUser(data, user, eventId);
        if (turtle instanceof BoardImpl board)
            updateBoard(data, board, eventId);
        if (turtle instanceof IssueImpl issue)
            updateIssue(data, issue, eventId);
        if (turtle instanceof TagImpl tag)
            updateTag(data, tag, eventId);
    }

    public static void updateGroup(@NotNull EntityData data, @NotNull GroupImpl group) {
        updateGroup(data, group, null);
    }

    public static void updateGroup(@NotNull EntityData data, @NotNull GroupImpl group, @Nullable Long eventId) {
        final JsonObject json = data.toJson();

        update(group, json, "name", JsonElement::getAsString, Group::getName, group::setName, eventId, GroupUpdateNameEvent::new);
        update(group, json, "position", JsonElement::getAsInt, Group::getPosition, group::setPosition, eventId, GroupUpdatePositionEvent::new);
        updateEntities(group, json, "members", group.getMembers(), eventId, GroupUpdateMembersEvent::new);
    }

    public static void updateProject(@NotNull EntityData data, @NotNull ProjectImpl project) {
        updateProject(data, project, null);
    }

    public static void updateProject(@NotNull EntityData data, @NotNull ProjectImpl project, @Nullable Long eventId) {
        final JsonObject json = data.toJson();

        update(project, json, "title", JsonElement::getAsString, Project::getTitle, project::setTitle, eventId, ProjectUpdateTitleEvent::new);
        update(project, json, "timings", j -> ProjectUtils.buildProjectTimings(j.getAsJsonObject()), Project::getTimings, project::setTimings, eventId, ProjectUpdateTimingsEvent::new);
        updateEnum(project, json, "state", Project.State.class, project.getState(), project::setState, eventId, ProjectUpdateStateEvent::new);
        updateEntities(project, json, "members", project.getMembers(), eventId, ProjectUpdateMembersEvent::new);
    }

    public static void updateTicket(@NotNull EntityData data, @NotNull TicketImpl ticket) {
        updateTicket(data, ticket, null);
    }

    public static void updateTicket(@NotNull EntityData data, @NotNull TicketImpl ticket, @Nullable Long eventId) {
        final JsonObject json = data.toJson();

        update(ticket, json, "title", JsonElement::getAsString, Ticket::getTitle, ticket::setTitle, eventId, TicketUpdateTitleEvent::new);
        updateEnum(ticket, json, "state", Ticket.State.class, ticket.getState(), ticket::setState, eventId, TicketUpdateStateEvent::new);
        updateArray(ticket, json, "tags", ArrayList::new, JsonElement::getAsString, ticket.getTags(), tags -> {
            ticket.getTagsMutable().clear();
            ticket.getTagsMutable().addAll(tags);
        }, eventId, TicketUpdateTagsEvent::new);
        updateEntities(ticket, json, "users", ticket.getUsers(), eventId, TicketUpdateUsersEvent::new);
    }

    public static void updateUser(@NotNull EntityData data, @NotNull UserImpl user) {
        updateUser(data, user, null);
    }

    public static void updateUser(@NotNull EntityData data, @NotNull UserImpl user, @Nullable Long eventId) {
        final JsonObject json = data.toJson();

        update(user, json, "name", JsonElement::getAsString, User::getName, user::setName, eventId, UserUpdateNameEvent::new);
        updateArray(user, json, "discord", ArrayList::new, JsonElement::getAsLong, user.getDiscordIds(), longs -> {
            user.getDiscordIdsMutable().clear();
            user.getDiscordIdsMutable().addAll(longs);
        }, eventId, UserUpdateDiscordIdsEvent::new);
        updateArray(user, json, "minecraft", ArrayList::new, j -> UUID.fromString(j.getAsString()), user.getMinecraftIds(), uuids -> {
            user.getMinecraftIdsMutable().clear();
            user.getMinecraftIdsMutable().addAll(uuids);
        }, eventId, UserUpdateMinecraftIdsEvent::new);
    }

    public static void updateBoard(@NotNull EntityData data, @NotNull BoardImpl board) {
        updateBoard(data, board, null);
    }

    public static void updateBoard(@NotNull EntityData data, @NotNull BoardImpl board, @Nullable Long eventId) {
        final JsonObject json = data.toJson();

        update(board, json, "title", JsonElement::getAsString, Board::getTitle, board::setTitle, eventId, BoardUpdateTitleEvent::new);
        update(board, json, "group", j -> board.getAPI().getGroup(j.getAsLong()), Board::getGroup, group -> board.setGroup(((GroupImpl) group)), eventId, BoardUpdateGroupEvent::new);
    }

    public static void updateIssue(@NotNull EntityData data, @NotNull IssueImpl issue) {
        updateIssue(data, issue, null);
    }

    public static void updateIssue(@NotNull EntityData data, @NotNull IssueImpl issue, @Nullable Long eventId) {
        final JsonObject json = data.toJson();

        updateEntities(issue, json, "assignees", issue.getAssignees(), eventId, IssueUpdateAssigneesEvent::new);
        update(issue, json, "title", JsonElement::getAsString, Issue::getTitle, issue::setTitle, eventId, IssueUpdateTitleEvent::new);
        updateEnum(issue, json, "state", Issue.State.class, issue.getState(), issue::setState, eventId, IssueUpdateStateEvent::new);
        updateEntities(issue, json, "tags", issue.getTags(), eventId, IssueUpdateTagsEvent::new);
    }

    public static void updateTag(@NotNull EntityData data, @NotNull TagImpl tag) {
        updateTag(data, tag, null);
    }

    public static void updateTag(@NotNull EntityData data, @NotNull TagImpl tag, @Nullable Long eventId) {
        final JsonObject json = data.toJson();

        update(tag, json, "name", JsonElement::getAsString, Tag::getName, tag::setName, eventId, TagUpdateNameEvent::new);
    }

    private static <T extends Turtle, E> void update(
            @NotNull T entity,
            @NotNull JsonObject json,
            @NotNull String path,
            @NotNull Function<JsonElement, E> jsonToObj,
            @NotNull Function<T, E> oldValProvider,
            @NotNull Consumer<E> newValConsumer,
            @Nullable Long eventId,
            @NotNull Function4<Long, T, E, E, ? extends EntityUpdateEvent<T, E>> eventBuilder
    ) {
        final JsonElement element = json.get(path);

        if (element == null) return;

        final E newVal = jsonToObj.apply(element);
        final E oldVal = oldValProvider.apply(entity);

        if (Objects.deepEquals(oldVal, newVal)) return;

        newValConsumer.accept(newVal);

        if (eventId == null) return;
        EntityUpdateEvent<T, E> event = eventBuilder.apply(eventId, entity, oldVal, newVal);
        entity.getAPI().getEventHandler().handle(event);
    }

    private static <T extends Turtle, E extends Enum<E>> void updateEnum(
            @NotNull T entity,
            @NotNull JsonObject json,
            @NotNull String path,
            @NotNull Class<E> enumType,
            @NotNull E oldVal,
            @NotNull Consumer<E> newValConsumer,
            @Nullable Long eventId,
            @NotNull Function4<Long, T, E, E, ? extends EntityUpdateEvent<?, E>> eventBuilder
    ) {
        final JsonElement element = json.get(path);

        if (element == null) return;

        final E newVal = Enum.valueOf(enumType, element.getAsString());

        if (Objects.deepEquals(oldVal, newVal)) return;

        newValConsumer.accept(newVal);

        if (eventId == null) return;
        EntityUpdateEvent<?, E> event = eventBuilder.apply(eventId, entity, oldVal, newVal);
        entity.getAPI().getEventHandler().handle(event);
    }

    private static <T extends Turtle, E, C extends Collection<E>> void updateArray(
            @NotNull T entity,
            @NotNull JsonObject json,
            @NotNull String path,
            @NotNull Supplier<C> collectionSupplier,
            @NotNull Function<JsonElement, E> elementParser,
            @NotNull C oldValues,
            @NotNull Consumer<Collection<? extends E>> newValConsumer,
            @Nullable Long eventId,
            @NotNull Function4<Long, T, C, C, ? extends EntityUpdateEvent<T, ?>> eventBuilder
    ) {
        final JsonArray array = json.getAsJsonArray(path);

        if (array == null) return;

        final C newValues = collectionSupplier.get();
        for (JsonElement element : array)
            newValues.add(elementParser.apply(element));

        if (oldValues.equals(newValues)) return;

        newValConsumer.accept(newValues);

        if (eventId == null) return;
        EntityUpdateEvent<T, ?> event = eventBuilder.apply(eventId, entity, oldValues, newValues);
        entity.getAPI().getEventHandler().handle(event);
    }

    private static <T extends Turtle, E extends TurtleImpl> void updateEntities(
            @NotNull T entity,
            @NotNull JsonObject json,
            @NotNull String path,
            @NotNull DelegatingTurtleCacheView<E> cacheView,
            @Nullable Long eventId,
            @NotNull Function4<Long, T, Set<E>, Set<E>, ? extends EntityUpdateEvent<T, ?>> eventBuilder
    ) {
        final JsonArray array = json.getAsJsonArray(path);

        if (array == null) return;

        final Set<E> oldValues = cacheView.getAsImmutableSet();
        final Set<E> newValues = new HashSet<>();
        for (JsonElement element : array)
            newValues.add(cacheView.getCache().get(element.getAsLong(), cacheView.getContentType()));

        cacheView.overwrite(newValues);

        if (eventId == null) return;
        EntityUpdateEvent<T, ?> event = eventBuilder.apply(eventId, entity, oldValues, newValues);
        entity.getAPI().getEventHandler().handle(event);
    }
}
