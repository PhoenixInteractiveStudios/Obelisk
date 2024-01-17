package org.burrow_studios.obelisk.core.entities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.core.ObeliskImpl;
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

import java.util.UUID;

import static org.burrow_studios.obelisk.core.entities.UpdateHelper.*;

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

        handleUpdate(json, "name", JsonElement::getAsString, group::setName);
        handleUpdate(json, "position", JsonElement::getAsInt, group::setPosition);
        handleUpdateTurtles(json, "members", group::getMembers);
    }

    public static void updateProject(@NotNull EntityData data, @NotNull ProjectImpl project) {
        updateProject(data, project, null);
    }

    public static void updateProject(@NotNull EntityData data, @NotNull ProjectImpl project, @Nullable Long eventId) {
        final JsonObject json = data.toJson();

        handleUpdate(json, "title", JsonElement::getAsString, project::setTitle);
        handleUpdateObject(json, "timings", project.getAPI(), ProjectUtils::buildProjectTimings, project::setTimings);
        handleUpdateEnum(json, "state", Project.State.class, project::setState);
        handleUpdateTurtles(json, "members", project::getMembers);
    }

    public static void updateTicket(@NotNull EntityData data, @NotNull TicketImpl ticket) {
        updateTicket(data, ticket, null);
    }

    public static void updateTicket(@NotNull EntityData data, @NotNull TicketImpl ticket, @Nullable Long eventId) {
        final JsonObject json = data.toJson();

        handleUpdate(json, "title", JsonElement::getAsString, ticket::setTitle);
        handleUpdateEnum(json, "state", Ticket.State.class, ticket::setState);
        handleUpdateArray(json, "tags", JsonElement::getAsString, ticket.getTagsMutable());
        handleUpdateTurtles(json, "users", ticket::getUsers);
    }

    public static void updateUser(@NotNull EntityData data, @NotNull UserImpl user) {
        updateUser(data, user, null);
    }

    public static void updateUser(@NotNull EntityData data, @NotNull UserImpl user, @Nullable Long eventId) {
        final JsonObject json = data.toJson();

        handleUpdate(json, "name", JsonElement::getAsString, user::setName);
        handleUpdateArray(json, "discord", JsonElement::getAsLong, user.getDiscordIdsMutable());
        handleUpdateArray(json, "minecraft", j -> UUID.fromString(j.getAsString()), user.getMinecraftIdsMutable());
    }

    public static void updateBoard(@NotNull EntityData data, @NotNull BoardImpl board) {
        updateBoard(data, board, null);
    }

    public static void updateBoard(@NotNull EntityData data, @NotNull BoardImpl board, @Nullable Long eventId) {
        final JsonObject json = data.toJson();

        handleUpdate(json, "title", JsonElement::getAsString, board::setTitle);
        handleUpdateTurtle(json, "group", board.getAPI(), ObeliskImpl::getGroup, board::setGroup);
        handleUpdateTurtles(json, "tags", board::getAvailableTags);
        handleUpdateTurtles(json, "issues", board::getIssues);
    }

    public static void updateIssue(@NotNull EntityData data, @NotNull IssueImpl issue) {
        updateIssue(data, issue, null);
    }

    public static void updateIssue(@NotNull EntityData data, @NotNull IssueImpl issue, @Nullable Long eventId) {
        final JsonObject json = data.toJson();

        handleUpdateTurtles(json, "assignees", issue::getAssignees);
        handleUpdate(json, "title", JsonElement::getAsString, issue::setTitle);
        handleUpdateEnum(json, "state", Issue.State.class, issue::setState);
        handleUpdateTurtles(json, "tags", issue::getTags);
    }

    public static void updateTag(@NotNull EntityData data, @NotNull TagImpl tag) {
        updateTag(data, tag, null);
    }

    public static void updateTag(@NotNull EntityData data, @NotNull TagImpl tag, @Nullable Long eventId) {
        final JsonObject json = data.toJson();

        handleUpdate(json, "name", JsonElement::getAsString, tag::setName);
    }
}
