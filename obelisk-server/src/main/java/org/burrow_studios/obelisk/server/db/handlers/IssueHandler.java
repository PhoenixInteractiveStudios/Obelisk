package org.burrow_studios.obelisk.server.db.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.IssueImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.TagImpl;
import org.burrow_studios.obelisk.core.source.Request;
import org.burrow_studios.obelisk.core.source.Response;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.EntityProvider;
import org.burrow_studios.obelisk.server.db.NoSuchEntryException;
import org.burrow_studios.obelisk.server.db.RequestHandler;
import org.burrow_studios.obelisk.server.db.entity.BoardDB;
import org.burrow_studios.obelisk.commons.turtle.TurtleGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public class IssueHandler implements RequestHandler {
    private final TurtleGenerator turtleGenerator;
    private final EntityProvider provider;

    public IssueHandler(@NotNull EntityProvider provider) {
        this.provider = provider;
        this.turtleGenerator = TurtleGenerator.get(provider.getClass().getSimpleName());
    }

    public @NotNull Response onGetAll(@NotNull Request request) {
        final long boardId = (long) request.getEndpoint().args()[1];

        final JsonArray issues = new JsonArray();
        final Set<Long> issueIds;

        try {
            issueIds = this.getDB().getIssueIds(boardId);
        } catch (DatabaseException e) {
            return request.respond(500, null);
        }

        for (long issueId : issueIds)
            issues.add(issueId);

        return request.respond(200, issues);
    }

    public @NotNull Response onGet(@NotNull Request request) {
        final long boardId = (long) request.getEndpoint().args()[1];
        final long issueId = (long) request.getEndpoint().args()[3];
        final JsonObject issue;

        try {
            issue = this.getDB().getIssue(boardId, issueId);
        } catch (NoSuchEntryException e) {
            return request.respond(404, null);
        } catch (DatabaseException e) {
            return request.respond(500, null);
        }

        return request.respond(200, issue);
    }

    public @NotNull Response onCreate(@NotNull Request request) {
        final long boardId = (long) request.getEndpoint().args()[1];

        final long id = this.turtleGenerator.newId();

        final JsonObject json = request.optionalContent()
                .map(JsonElement::getAsJsonObject)
                .orElse(null);

        if (json == null)
            return request.respond(400, null);

        json.addProperty("id", id);
        json.addProperty("board", boardId);

        final IssueImpl issue = new IssueImpl(provider.getAPI(), new EntityData(json));

        this.getDB().createIssue(
                issue.getBoard().getId(),
                issue.getId(),
                issue.getAuthor().getId(),
                issue.getTitle(),
                issue.getState()
        );

        for (UserImpl assignee : issue.getAssignees())
            this.getDB().addIssueAssignee(issue.getBoard().getId(), issue.getId(), assignee.getId());

        for (TagImpl tag : issue.getTags())
            this.getDB().addIssueTag(issue.getBoard().getId(), issue.getId(), tag.getId());

        return request.respond(200, issue.toJson());
    }

    public @NotNull Response onAddAssignee(@NotNull Request request) {
        final long boardId = (long) request.getEndpoint().args()[1];
        final long issueId = (long) request.getEndpoint().args()[3];
        final long  userId = (long) request.getEndpoint().args()[5];

        this.getDB().addIssueAssignee(boardId, issueId, userId);

        return request.respond(204, null);
    }

    public @NotNull Response onDeleteAssignee(@NotNull Request request) {
        final long boardId = (long) request.getEndpoint().args()[1];
        final long issueId = (long) request.getEndpoint().args()[3];
        final long  userId = (long) request.getEndpoint().args()[5];

        this.getDB().removeIssueAssignee(boardId, issueId, userId);

        return request.respond(204, null);
    }

    public @NotNull Response onAddTag(@NotNull Request request) {
        final long boardId = (long) request.getEndpoint().args()[1];
        final long issueId = (long) request.getEndpoint().args()[3];
        final long   tagId = (long) request.getEndpoint().args()[5];

        this.getDB().addIssueTag(boardId, issueId, tagId);

        return request.respond(204, null);
    }

    public @NotNull Response onDeleteTag(@NotNull Request request) {
        final long boardId = (long) request.getEndpoint().args()[1];
        final long issueId = (long) request.getEndpoint().args()[3];
        final long   tagId = (long) request.getEndpoint().args()[5];

        this.getDB().removeIssueTag(boardId, issueId, tagId);

        return request.respond(204, null);
    }

    public @NotNull Response onDelete(@NotNull Request request) {
        final long boardId = (long) request.getEndpoint().args()[1];
        final long issueId = (long) request.getEndpoint().args()[3];

        this.getDB().deleteIssue(boardId, issueId);

        return request.respond(204, null);
    }

    public @NotNull Response onEdit(@NotNull Request request) {
        final long boardId = (long) request.getEndpoint().args()[1];
        final long issueId = (long) request.getEndpoint().args()[3];
        final JsonObject json = request.optionalContent()
                .map(JsonElement::getAsJsonObject)
                .orElse(null);

        if (json == null)
            return request.respond(400, null);

        Optional.ofNullable(json.get("title"))
                .map(JsonElement::getAsString)
                .ifPresent(title -> getDB().updateIssueTitle(boardId, issueId, title));

        Optional.ofNullable(json.get("state"))
                .map(JsonElement::getAsString)
                .map(Issue.State::valueOf)
                .ifPresent(state -> getDB().updateIssueState(boardId, issueId, state));

        final JsonObject issue = getDB().getIssue(boardId, issueId);
        return request.respond(200, issue);
    }

    private @NotNull BoardDB getDB() {
        return this.provider.getBoardDB();
    }
}
