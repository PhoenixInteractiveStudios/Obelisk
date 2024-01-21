package org.burrow_studios.obelisk.core.entities;

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
import org.burrow_studios.obelisk.core.entities.action.project.ProjectUtils;
import org.burrow_studios.obelisk.core.entities.impl.GroupImpl;
import org.burrow_studios.obelisk.core.entities.impl.ProjectImpl;
import org.burrow_studios.obelisk.core.entities.impl.TicketImpl;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.BoardImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.IssueImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.TagImpl;
import org.burrow_studios.obelisk.util.function.Function4;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

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

        Optional.ofNullable(json.get("members"))
                .map(JsonElement::getAsJsonArray)
                .map(elements -> {
                    final Set<UserImpl> users = new HashSet<>();
                    for (JsonElement element : elements)
                        users.add(group.getAPI().getUser(element.getAsLong()));
                    return users;
                })
                .ifPresent(newMembers -> {
                    final Set<UserImpl> oldMembers = group.getMembers().getAsImmutableSet();

                    if (oldMembers.equals(newMembers)) return;

                    group.getMembers().overwrite(newMembers);

                    if (eventId == null) return;
                    final GroupUpdateMembersEvent event = new GroupUpdateMembersEvent(eventId, group, oldMembers, newMembers);
                    group.getAPI().getEventHandler().handle(event);
                });
    }

    public static void updateProject(@NotNull EntityData data, @NotNull ProjectImpl project) {
        updateProject(data, project, null);
    }

    public static void updateProject(@NotNull EntityData data, @NotNull ProjectImpl project, @Nullable Long eventId) {
        final JsonObject json = data.toJson();

        update(project, json, "title", JsonElement::getAsString, Project::getTitle, project::setTitle, eventId, ProjectUpdateTitleEvent::new);
        update(project, json, "timings", j -> ProjectUtils.buildProjectTimings(j.getAsJsonObject()), Project::getTimings, project::setTimings, eventId, ProjectUpdateTimingsEvent::new);
        update(project, json, "state", j -> Project.State.valueOf(j.getAsString()), Project::getState, project::setState, eventId, ProjectUpdateStateEvent::new);

        Optional.ofNullable(json.get("members"))
                .map(JsonElement::getAsJsonArray)
                .map(elements -> {
                    final Set<UserImpl> users = new HashSet<>();
                    for (JsonElement element : elements)
                        users.add(project.getAPI().getUser(element.getAsLong()));
                    return users;
                })
                .ifPresent(newMembers -> {
                    final Set<UserImpl> oldMembers = project.getMembers().getAsImmutableSet();

                    if (oldMembers.equals(newMembers)) return;

                    project.getMembers().overwrite(newMembers);

                    if (eventId == null) return;
                    final ProjectUpdateMembersEvent event = new ProjectUpdateMembersEvent(eventId, project, oldMembers, newMembers);
                    project.getAPI().getEventHandler().handle(event);
                });
    }

    public static void updateTicket(@NotNull EntityData data, @NotNull TicketImpl ticket) {
        updateTicket(data, ticket, null);
    }

    public static void updateTicket(@NotNull EntityData data, @NotNull TicketImpl ticket, @Nullable Long eventId) {
        final JsonObject json = data.toJson();

        update(ticket, json, "title", JsonElement::getAsString, Ticket::getTitle, ticket::setTitle, eventId, TicketUpdateTitleEvent::new);
        update(ticket, json, "state", j -> Ticket.State.valueOf(j.getAsString()), Ticket::getState, ticket::setState, eventId, TicketUpdateStateEvent::new);

        Optional.ofNullable(json.get("tags"))
                .map(JsonElement::getAsJsonArray)
                .map(elements -> {
                    ArrayList<String> tags = new ArrayList<>();
                    for (JsonElement element : elements)
                        tags.add(element.getAsString());
                    return tags;
                })
                .ifPresent(newTags -> {
                    final List<String> oldTags = ticket.getTags();

                    if (oldTags.equals(newTags)) return;

                    ticket.getTagsMutable().clear();
                    ticket.getTagsMutable().addAll(newTags);

                    if (eventId == null) return;
                    final TicketUpdateTagsEvent event = new TicketUpdateTagsEvent(eventId, ticket, oldTags, newTags);
                    ticket.getAPI().getEventHandler().handle(event);
                });

        Optional.ofNullable(json.get("members"))
                .map(JsonElement::getAsJsonArray)
                .map(elements -> {
                    final Set<UserImpl> users = new HashSet<>();
                    for (JsonElement element : elements)
                        users.add(ticket.getAPI().getUser(element.getAsLong()));
                    return users;
                })
                .ifPresent(newUsers -> {
                    final Set<UserImpl> oldUsers = ticket.getUsers().getAsImmutableSet();

                    if (oldUsers.equals(newUsers)) return;

                    ticket.getUsers().overwrite(newUsers);

                    if (eventId == null) return;
                    final TicketUpdateUsersEvent event = new TicketUpdateUsersEvent(eventId, ticket, oldUsers, newUsers);
                    ticket.getAPI().getEventHandler().handle(event);
                });
    }

    public static void updateUser(@NotNull EntityData data, @NotNull UserImpl user) {
        updateUser(data, user, null);
    }

    public static void updateUser(@NotNull EntityData data, @NotNull UserImpl user, @Nullable Long eventId) {
        final JsonObject json = data.toJson();

        update(user, json, "name", JsonElement::getAsString, User::getName, user::setName, eventId, UserUpdateNameEvent::new);

        Optional.ofNullable(json.get("discord"))
                .map(JsonElement::getAsJsonArray)
                .map(elements -> {
                    ArrayList<Long> tags = new ArrayList<>();
                    for (JsonElement element : elements)
                        tags.add(element.getAsLong());
                    return tags;
                })
                .ifPresent(newDiscord -> {
                    final List<Long> oldDiscord = user.getDiscordIds();

                    if (oldDiscord.equals(newDiscord)) return;

                    user.getDiscordIdsMutable().clear();
                    user.getDiscordIdsMutable().addAll(newDiscord);

                    if (eventId == null) return;
                    final UserUpdateDiscordIdsEvent event = new UserUpdateDiscordIdsEvent(eventId, user, oldDiscord, newDiscord);
                    user.getAPI().getEventHandler().handle(event);
                });

        Optional.ofNullable(json.get("minecraft"))
                .map(JsonElement::getAsJsonArray)
                .map(elements -> {
                    ArrayList<UUID> tags = new ArrayList<>();
                    for (JsonElement element : elements)
                        tags.add(UUID.fromString(element.getAsString()));
                    return tags;
                })
                .ifPresent(newMinecraft -> {
                    final List<UUID> oldMinecraft = user.getMinecraftIds();

                    if (oldMinecraft.equals(newMinecraft)) return;

                    user.getMinecraftIdsMutable().clear();
                    user.getMinecraftIdsMutable().addAll(newMinecraft);

                    if (eventId == null) return;
                    final UserUpdateMinecraftIdsEvent event = new UserUpdateMinecraftIdsEvent(eventId, user, oldMinecraft, newMinecraft);
                    user.getAPI().getEventHandler().handle(event);
                });
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

        Optional.ofNullable(json.get("assignees"))
                .map(JsonElement::getAsJsonArray)
                .map(elements -> {
                    final Set<UserImpl> users = new HashSet<>();
                    for (JsonElement element : elements)
                        users.add(issue.getAPI().getUser(element.getAsLong()));
                    return users;
                })
                .ifPresent(newAssignees -> {
                    final Set<UserImpl> oldAssignees = issue.getAssignees().getAsImmutableSet();

                    if (oldAssignees.equals(newAssignees)) return;

                    issue.getAssignees().overwrite(newAssignees);

                    if (eventId == null) return;
                    final IssueUpdateAssigneesEvent event = new IssueUpdateAssigneesEvent(eventId, issue, oldAssignees, newAssignees);
                    issue.getAPI().getEventHandler().handle(event);
                });

        update(issue, json, "title", JsonElement::getAsString, Issue::getTitle, issue::setTitle, eventId, IssueUpdateTitleEvent::new);
        update(issue, json, "state", j -> Issue.State.valueOf(j.getAsString()), Issue::getState, issue::setState, eventId, IssueUpdateStateEvent::new);

        Optional.ofNullable(json.get("tags"))
                .map(JsonElement::getAsJsonArray)
                .map(elements -> {
                    final Set<TagImpl> tags = new HashSet<>();
                    for (JsonElement element : elements)
                        tags.add(issue.getAPI().getTag(element.getAsLong()));
                    return tags;
                })
                .ifPresent(newTags -> {
                    final Set<TagImpl> oldTags = issue.getTags().getAsImmutableSet();

                    if (oldTags.equals(newTags)) return;

                    issue.getTags().overwrite(newTags);

                    if (eventId == null) return;
                    final IssueUpdateTagsEvent event = new IssueUpdateTagsEvent(eventId, issue, oldTags, newTags);
                    issue.getAPI().getEventHandler().handle(event);
                });
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
}
