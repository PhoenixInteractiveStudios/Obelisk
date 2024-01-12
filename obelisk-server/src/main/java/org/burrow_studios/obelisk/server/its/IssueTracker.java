package org.burrow_studios.obelisk.server.its;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.core.entities.checks.board.BoardChecks;
import org.burrow_studios.obelisk.core.entities.checks.board.IssueChecks;
import org.burrow_studios.obelisk.core.entities.checks.board.TagChecks;
import org.burrow_studios.obelisk.util.TurtleGenerator;
import org.burrow_studios.obelisk.server.ObeliskServer;
import org.burrow_studios.obelisk.server.db.Cache;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.entity.BoardDB;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public class IssueTracker {
    private final ObeliskServer server;

    private final BoardDB database;

    private final Cache<JsonObject> boardCache;
    private final Cache<JsonObject> issueCache;
    private final Cache<JsonObject>   tagCache;

    private final TurtleGenerator turtleGenerator;

    public IssueTracker(@NotNull ObeliskServer server) {
        this.server = server;

        this.database = server.getEntityProvider().getBoardDB();

        this.boardCache = new Cache<>();
        this.issueCache = new Cache<>();
        this.tagCache   = new Cache<>();

        this.turtleGenerator = TurtleGenerator.get("Bruno");

        for (long board : this.database.getBoardIds()) {
            this.boardCache.add(board);

            this.database.getIssueIds(board).forEach(issueCache::add);
            this.database.getTagIds(board).forEach(tagCache::add);
        }
    }

    public @NotNull Set<Long> getBoards() {
        return this.boardCache.getIds();
    }

    public @NotNull Set<Long> getIssues(long board) {
        return this.database.getIssueIds(board);
    }

    public @NotNull Set<Long> getTags(long board) {
        return this.database.getTagIds(board);
    }

    public @NotNull JsonObject getBoard(long id) throws DatabaseException {
        return this.boardCache.get(id)
                .orElseGet(() -> this.retrieveBoard(id));
    }

    public @NotNull JsonObject getIssue(long board, long id) throws DatabaseException {
        return this.issueCache.get(id)
                .orElseGet(() -> this.retrieveIssue(board, id));
    }

    public @NotNull JsonObject getTag(long board, long id) throws DatabaseException {
        return this.tagCache.get(id)
                .orElseGet(() -> this.retrieveTag(board, id));
    }

    private @NotNull JsonObject retrieveBoard(long id) throws DatabaseException {
        final JsonObject result = this.database.getBoard(id);
        this.boardCache.put(id, result);
        return result;
    }

    private @NotNull JsonObject retrieveIssue(long board, long id) throws DatabaseException {
        final JsonObject result = this.database.getIssue(board, id);
        this.issueCache.put(id, result);
        return result;
    }

    private @NotNull JsonObject retrieveTag(long board, long id) throws DatabaseException {
        final JsonObject result = this.database.getTag(board, id);
        this.tagCache.put(id, result);
        return result;
    }

    public @NotNull JsonObject createBoard(@NotNull JsonObject json) throws DatabaseException {
        final long id = this.turtleGenerator.newId();

        final String title = json.get("title").getAsString();
        final long   group = json.get("group").getAsLong();

        BoardChecks.checkTitle(title);

        this.database.createBoard(id, title, group);
        return this.retrieveBoard(id);
    }

    public @NotNull JsonObject createIssue(long board, @NotNull JsonObject json) throws DatabaseException {
        final long id = this.turtleGenerator.newId();

        final long   author   = json.get("author").getAsLong();
        final String title    = json.get("title").getAsString();
        final String stateStr = json.get("state").getAsString();

        final IssueState state = IssueState.valueOf(stateStr);

        IssueChecks.checkTitle(title);

        this.database.createIssue(board, id, author, title, state);
        return this.retrieveIssue(board, id);
    }

    public @NotNull JsonObject createTag(long board, @NotNull JsonObject json) throws DatabaseException {
        final long id = this.turtleGenerator.newId();

        final String name = json.get("name").getAsString();

        TagChecks.checkName(name);

        this.database.createTag(board, id, name);
        return this.retrieveTag(board, id);
    }

    public void patchBoard(long id, @NotNull JsonObject json) throws DatabaseException {
        Optional.ofNullable(json.get("title"))
                .map(JsonElement::getAsString)
                .ifPresent(title -> {
                    BoardChecks.checkTitle(title);
                    database.updateBoardTitle(id, title);
                });

        Optional.ofNullable(json.get("group"))
                .map(JsonElement::getAsLong)
                .ifPresent(group -> database.updateBoardGroup(id, group));

        this.retrieveBoard(id);
    }

    public void patchIssue(long board, long id, @NotNull JsonObject json) throws DatabaseException {
        Optional.ofNullable(json.get("title"))
                .map(JsonElement::getAsString)
                .ifPresent(title -> {
                    IssueChecks.checkTitle(title);
                    database.updateIssueTitle(board, id, title);
                });

        Optional.ofNullable(json.get("state"))
                .map(JsonElement::getAsString)
                .map(IssueState::valueOf)
                .ifPresent(state -> database.updateIssueState(board, id, state));

        this.retrieveIssue(board, id);
    }

    public void patchTag(long board, long id, @NotNull JsonObject json) throws DatabaseException {
        Optional.ofNullable(json.get("name"))
                .map(JsonElement::getAsString)
                .ifPresent(name -> {
                    TagChecks.checkName(name);
                    database.updateTagTitle(board, id, name);
                });

        this.retrieveTag(board, id);
    }

    public void addIssueAssignee(long board, long id, long assignee) throws DatabaseException {
        this.database.addIssueAssignee(board, id, assignee);
        this.retrieveIssue(board, id);
    }

    public void removeIssueAssignee(long board, long id, long assignee) throws DatabaseException {
        this.database.removeIssueAssignee(board, id, assignee);
        this.retrieveIssue(board, id);
    }

    public void addIssueTag(long board, long id, long tag) throws DatabaseException {
        this.database.addIssueTag(board, id, tag);
        this.retrieveIssue(board, id);
    }

    public void removeIssueTag(long board, long id, long tag) throws DatabaseException {
        this.database.removeIssueTag(board, id, tag);
        this.retrieveIssue(board, id);
    }
}
