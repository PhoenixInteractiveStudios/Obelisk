package org.burrow_studios.obelisk.internal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.Turtle;
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
import java.util.function.Function;

public class EntityBuilder {
    private final ObeliskImpl api;

    public EntityBuilder(@NotNull ObeliskImpl api) {
        this.api = api;
    }

    public @NotNull GroupImpl buildGroup(@NotNull JsonObject json) {
        final long   id       = json.get("id").getAsLong();
        final String name     = json.get("name").getAsString();
        final int    position = json.get("position").getAsInt();

        final DelegatingTurtleCacheView<UserImpl> members = getDelegatingCacheView(json, "members", api.getUsers(), UserImpl.class);

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

        final DelegatingTurtleCacheView<UserImpl> members = getDelegatingCacheView(json, "members", api.getUsers(), UserImpl.class);

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

        final ArrayList<String> tags = getList(json, "tags", JsonElement::getAsString);

        final DelegatingTurtleCacheView<UserImpl> users = getDelegatingCacheView(json, "users", api.getUsers(), UserImpl.class);

        final TicketImpl ticket = new TicketImpl(api, id, title, state, tags, users);

        this.api.getTickets().add(ticket);
        return ticket;
    }

    public @NotNull UserImpl buildUser(@NotNull JsonObject json) {
        final long   id   = json.get("id").getAsLong();
        final String name = json.get("name").getAsString();

        final ArrayList<Long>   discordIds = getList(json, "discord", JsonElement::getAsLong);
        final ArrayList<UUID> minecraftIds = getList(json, "minecraft", e -> UUID.fromString(e.getAsString()));

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

        final DelegatingTurtleCacheView<UserImpl> assignees = getDelegatingCacheView(json, "assignees", api.getUsers(), UserImpl.class);
        final Issue.State state = Issue.State.valueOf(stateStr);

        final BoardImpl board = this.api.getBoard(boardId);
        assert board != null;

        final TurtleCache<TagImpl> availableTags = board.getAvailableTags();
        final DelegatingTurtleCacheView<TagImpl> tags = getDelegatingCacheView(json, "tags", availableTags, TagImpl.class);

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

    private static <T extends Turtle> @NotNull DelegatingTurtleCacheView<T> getDelegatingCacheView(@NotNull JsonObject json, @NotNull String path, @NotNull TurtleCache<? super T> cache, @NotNull Class<T> type) {
        return getDelegatingCacheView(json.getAsJsonArray(path), cache, type);
    }

    private static <T extends Turtle> @NotNull DelegatingTurtleCacheView<T> getDelegatingCacheView(@NotNull JsonArray ids, @NotNull TurtleCache<? super T> cache, @NotNull Class<T> type) {
        final DelegatingTurtleCacheView<T> entities = new DelegatingTurtleCacheView<>(cache, type);
        for (JsonElement idElement : ids)
            entities.add(cache.get(idElement.getAsLong(), type));
        return entities;
    }

    private static <T> @NotNull ArrayList<T> getList(@NotNull JsonObject json, @NotNull String path, @NotNull Function<JsonElement, T> mappingFunction) {
        return getList(json.getAsJsonArray(path), mappingFunction);
    }

    private static <T> @NotNull ArrayList<T> getList(@NotNull JsonArray elements, @NotNull Function<JsonElement, T> mappingFunction) {
        ArrayList<T> list = new ArrayList<>();
        for (JsonElement element : elements)
            list.add(mappingFunction.apply(element));
        return list;
    }
}
