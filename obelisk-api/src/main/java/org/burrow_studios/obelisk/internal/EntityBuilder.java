package org.burrow_studios.obelisk.internal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.issue.Issue;
import org.burrow_studios.obelisk.internal.cache.DelegatingTurtleCacheView;
import org.burrow_studios.obelisk.internal.cache.TurtleCache;
import org.burrow_studios.obelisk.internal.entities.GroupImpl;
import org.burrow_studios.obelisk.internal.entities.ProjectImpl;
import org.burrow_studios.obelisk.internal.entities.TicketImpl;
import org.burrow_studios.obelisk.internal.entities.UserImpl;
import org.burrow_studios.obelisk.internal.entities.issue.BoardImpl;
import org.burrow_studios.obelisk.internal.entities.issue.IssueImpl;
import org.burrow_studios.obelisk.internal.entities.issue.TagImpl;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

public class EntityBuilder {
    private final ObeliskImpl api;

    public EntityBuilder(@NotNull ObeliskImpl api) {
        this.api = api;
    }

    public @NotNull GroupImpl buildGroup(@NotNull JsonObject json) {
        final long   id       = json.get("id").getAsLong();
        final String name     = json.get("name").getAsString();
        final int    position = json.get("position").getAsInt();

        final JsonArray memberIds = json.getAsJsonArray("members");
        final DelegatingTurtleCacheView<UserImpl> members = new DelegatingTurtleCacheView<>(api.getUsers(), UserImpl.class);
        for (JsonElement memberId : memberIds)
            members.add(api.getUser(memberId.getAsLong()));

        final GroupImpl group = new GroupImpl(api, id, name, members, position);

        this.api.getGroups().add(group);
        return group;
    }

    public @NotNull ProjectImpl buildProject(@NotNull JsonObject json) {
        final long   id       = json.get("id").getAsLong();
        final String title    = json.get("title").getAsString();
        final String stateStr = json.get("state").getAsString();

        final Project.Timings timings = this.buildProjectTimings(json.getAsJsonObject("timings"));

        final Project.State state = Project.State.valueOf(stateStr);

        final JsonArray memberIds = json.getAsJsonArray("members");
        final DelegatingTurtleCacheView<UserImpl> members = new DelegatingTurtleCacheView<>(api.getUsers(), UserImpl.class);
        for (JsonElement memberId : memberIds)
            members.add(api.getUser(memberId.getAsLong()));

        final ProjectImpl project = new ProjectImpl(api, id, title, timings, state, members);

        this.api.getProjects().add(project);
        return project;
    }

    public @NotNull Project.Timings buildProjectTimings(@NotNull JsonObject json) {
        final String releaseStr = json.get("release").getAsString();
        final String   applyStr = json.get("apply").getAsString();
        final String   startStr = json.get("start").getAsString();
        final String     endStr = json.get("end").getAsString();

        final Instant release = Instant.parse(releaseStr);
        final Instant apply   = Instant.parse(applyStr);
        final Instant start   = Instant.parse(startStr);
        final Instant end     = Instant.parse(endStr);

        return new Project.Timings(release, apply, start, end);
    }

    public @NotNull TicketImpl buildTicket(@NotNull JsonObject json) {
        final long   id       = json.get("id").getAsLong();
        final String title    = json.get("title").getAsString();
        final String stateStr = json.get("state").getAsString();

        final Ticket.State state = Ticket.State.valueOf(stateStr);

        final JsonArray tagArray = json.getAsJsonArray("tags");
        final ArrayList<String> tags = new ArrayList<>(tagArray.size());
        for (JsonElement idElement : tagArray)
            tags.add(idElement.getAsString());

        final JsonArray userIds = json.getAsJsonArray("users");
        final DelegatingTurtleCacheView<UserImpl> users = new DelegatingTurtleCacheView<>(api.getUsers(), UserImpl.class);
        for (JsonElement userId : userIds)
            users.add(api.getUser(userId.getAsLong()));

        final TicketImpl ticket = new TicketImpl(api, id, title, state, tags, users);

        this.api.getTickets().add(ticket);
        return ticket;
    }

    public @NotNull UserImpl buildUser(@NotNull JsonObject json) {
        final long   id   = json.get("id").getAsLong();
        final String name = json.get("name").getAsString();

        final JsonArray discordIdArray = json.getAsJsonArray("discord");
        final ArrayList<Long> discordIds = new ArrayList<>(discordIdArray.size());
        for (JsonElement idElement : discordIdArray)
            discordIds.add(idElement.getAsLong());

        final JsonArray minecraftIdArray = json.getAsJsonArray("minecraft");
        final ArrayList<UUID> minecraftIds = new ArrayList<>(minecraftIdArray.size());
        for (JsonElement idElement : minecraftIdArray)
            minecraftIds.add(UUID.fromString(idElement.getAsString()));

        final UserImpl user = new UserImpl(api, id, name, discordIds, minecraftIds);

        this.api.getUsers().add(user);
        return user;
    }

    public @NotNull BoardImpl buildBoard(@NotNull JsonObject json) {
        final long   id      = json.get("id").getAsLong();
        final String title   = json.get("title").getAsString();
        final long   groupId = json.get("group").getAsLong();

        // TODO: figure out chronology of deserialization (resolve circular dependency)
        final TurtleCache<TagImpl>   availableTags = new TurtleCache<>(api);
        final TurtleCache<IssueImpl> issues        = new TurtleCache<>(api);

        final BoardImpl board = new BoardImpl(api, id, title, groupId, availableTags, issues);

        this.api.getBoards().add(board);
        return board;
    }

    public @NotNull IssueImpl buildIssue(@NotNull JsonObject json) {
        final long   id       = json.get("id").getAsLong();
        final long   boardId  = json.get("board").getAsLong();
        final long   authorId = json.get("author").getAsLong();
        final String title    = json.get("title").getAsString();
        final String stateStr = json.get("state").getAsString();

        final JsonArray assigneeIds = json.getAsJsonArray("assignees");
        final DelegatingTurtleCacheView<UserImpl> assignees = new DelegatingTurtleCacheView<>(api.getUsers(), UserImpl.class);
        for (JsonElement assigneeId : assigneeIds)
            assignees.add(api.getUser(assigneeId.getAsLong()));

        final Issue.State state = Issue.State.valueOf(stateStr);

        final BoardImpl board = this.api.getBoard(boardId);
        assert board != null;

        final TurtleCache<TagImpl> availableTags = board.getAvailableTags();

        final JsonArray tagArray = json.getAsJsonArray("tags");
        final DelegatingTurtleCacheView<TagImpl> tags = new DelegatingTurtleCacheView<>(availableTags, TagImpl.class);
        for (JsonElement tag : tagArray)
            tags.add(availableTags.get(tag.getAsLong()));

        final IssueImpl issue = new IssueImpl(api, id, boardId, authorId, assignees, title, state, tags);

        board.getIssues().add(issue);
        return issue;
    }

    public @NotNull TagImpl buildTag(@NotNull JsonObject json) {
        final long   id      = json.get("id").getAsLong();
        final String name    = json.get("name").getAsString();
        final long   boardId = json.get("board").getAsLong();

        final TagImpl tag = new TagImpl(api, id, boardId, name);

        final BoardImpl board = this.api.getBoard(boardId);
        assert board != null;

        board.getAvailableTags().add(tag);
        return tag;
    }
}
