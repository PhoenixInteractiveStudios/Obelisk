package org.burrow_studios.obelisk.core.entities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.api.entities.board.Issue;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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

        Optional.ofNullable(json.get("name"))
                .map(JsonElement::getAsString)
                .ifPresent(newName -> {
                    final String oldName = group.getName();
                    if (Objects.equals(oldName, newName)) return;

                    group.setName(newName);

                    if (eventId == null) return;
                    final GroupUpdateNameEvent event = new GroupUpdateNameEvent(eventId, group, oldName, newName);
                    group.getAPI().getEventHandler().handle(event);
                });

        Optional.ofNullable(json.get("position"))
                .map(JsonElement::getAsInt)
                .ifPresent(newPosition -> {
                    final int oldPosition = group.getPosition();
                    if (oldPosition == newPosition) return;

                    group.setPosition(newPosition);

                    if (eventId == null) return;
                    final GroupUpdatePositionEvent event = new GroupUpdatePositionEvent(eventId, group, oldPosition, newPosition);
                    group.getAPI().getEventHandler().handle(event);
                });

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

        Optional.ofNullable(json.get("title"))
                .map(JsonElement::getAsString)
                .ifPresent(newTitle -> {
                    final String oldTitle = project.getTitle();
                    if (Objects.equals(oldTitle, newTitle)) return;

                    project.setTitle(newTitle);

                    if (eventId == null) return;
                    final ProjectUpdateTitleEvent event = new ProjectUpdateTitleEvent(eventId, project, oldTitle, newTitle);
                    project.getAPI().getEventHandler().handle(event);
                });

        Optional.ofNullable(json.get("timings"))
                .map(JsonElement::getAsJsonObject)
                .map(ProjectUtils::buildProjectTimings)
                .ifPresent(newTimings -> {
                    final Project.Timings oldTimings = project.getTimings();
                    if (Objects.equals(oldTimings, newTimings)) return;

                    project.setTimings(newTimings);

                    if (eventId == null) return;
                    final ProjectUpdateTimingsEvent event = new ProjectUpdateTimingsEvent(eventId, project, oldTimings, newTimings);
                    project.getAPI().getEventHandler().handle(event);
                });

        Optional.ofNullable(json.get("state"))
                .map(JsonElement::getAsString)
                .map(Project.State::valueOf)
                .ifPresent(newState -> {
                    final Project.State oldState = project.getState();
                    if (Objects.equals(oldState, newState)) return;

                    project.setState(newState);

                    if (eventId == null) return;
                    final ProjectUpdateStateEvent event = new ProjectUpdateStateEvent(eventId, project, oldState, newState);
                    project.getAPI().getEventHandler().handle(event);
                });

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

        Optional.ofNullable(json.get("title"))
                .map(JsonElement::getAsString)
                .ifPresent(newTitle -> {
                    final String oldTitle = ticket.getTitle();
                    if (Objects.deepEquals(oldTitle, newTitle)) return;

                    ticket.setTitle(newTitle);

                    if (eventId == null) return;
                    final TicketUpdateTitleEvent event = new TicketUpdateTitleEvent(eventId, ticket, oldTitle, newTitle);
                    ticket.getAPI().getEventHandler().handle(event);
                });

        Optional.ofNullable(json.get("state"))
                .map(JsonElement::getAsString)
                .map(Ticket.State::valueOf)
                .ifPresent(newState -> {
                    final Ticket.State oldState = ticket.getState();
                    if (Objects.equals(oldState, newState)) return;

                    ticket.setState(newState);

                    if (eventId == null) return;
                    final TicketUpdateStateEvent event = new TicketUpdateStateEvent(eventId, ticket, oldState, newState);
                    ticket.getAPI().getEventHandler().handle(event);
                });

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

        Optional.ofNullable(json.get("name"))
                .map(JsonElement::getAsString)
                .ifPresent(newName -> {
                    final String oldName = user.getName();
                    if (Objects.equals(oldName, newName)) return;

                    user.setName(newName);

                    if (eventId == null) return;
                    final UserUpdateNameEvent event = new UserUpdateNameEvent(eventId, user, oldName, newName);
                    user.getAPI().getEventHandler().handle(event);
                });

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

        Optional.ofNullable(json.get("title"))
                .map(JsonElement::getAsString)
                .ifPresent(newTitle -> {
                    final String oldTitle = board.getTitle();
                    if (Objects.deepEquals(oldTitle, newTitle)) return;

                    board.setTitle(newTitle);

                    if (eventId == null) return;
                    final BoardUpdateTitleEvent event = new BoardUpdateTitleEvent(eventId, board, oldTitle, newTitle);
                    board.getAPI().getEventHandler().handle(event);
                });

        Optional.ofNullable(json.get("group"))
                .map(JsonElement::getAsLong)
                .map(id -> board.getAPI().getGroup(id))
                .ifPresent(newGroup -> {
                    final Group oldGroup = board.getGroup();
                    if (Objects.deepEquals(oldGroup, newGroup)) return;

                    board.setGroup(newGroup);

                    if (eventId == null) return;
                    final BoardUpdateGroupEvent event = new BoardUpdateGroupEvent(eventId, board, oldGroup, newGroup);
                    board.getAPI().getEventHandler().handle(event);
                });
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

        Optional.ofNullable(json.get("title"))
                .map(JsonElement::getAsString)
                .ifPresent(newTitle -> {
                    final String oldTitle = issue.getTitle();
                    if (Objects.deepEquals(oldTitle, newTitle)) return;

                    issue.setTitle(newTitle);

                    if (eventId == null) return;
                    final IssueUpdateTitleEvent event = new IssueUpdateTitleEvent(eventId, issue, oldTitle, newTitle);
                    issue.getAPI().getEventHandler().handle(event);
                });

        Optional.ofNullable(json.get("state"))
                .map(JsonElement::getAsString)
                .map(Issue.State::valueOf)
                .ifPresent(newState -> {
                    final Issue.State oldState = issue.getState();
                    if (Objects.equals(oldState, newState)) return;

                    issue.setState(newState);

                    if (eventId == null) return;
                    final IssueUpdateStateEvent event = new IssueUpdateStateEvent(eventId, issue, oldState, newState);
                    issue.getAPI().getEventHandler().handle(event);
                });

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

        Optional.ofNullable(json.get("name"))
                .map(JsonElement::getAsString)
                .ifPresent(newName -> {
                    final String oldName = tag.getName();
                    if (Objects.equals(oldName, newName)) return;

                    tag.setName(newName);

                    if (eventId == null) return;
                    final TagUpdateNameEvent event = new TagUpdateNameEvent(eventId, tag, oldName, newName);
                    tag.getAPI().getEventHandler().handle(event);
                });
    }
}
