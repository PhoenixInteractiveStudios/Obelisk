package org.burrow_studios.obelisk.bruno.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.bruno.Bruno;
import org.burrow_studios.obelisk.bruno.database.Database;
import org.burrow_studios.obelisk.bruno.database.IssueState;
import org.burrow_studios.obelisk.bruno.exceptions.NoSuchEntryException;
import org.burrow_studios.obelisk.commons.function.ExceptionalFunction;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.Status;
import org.burrow_studios.obelisk.commons.rpc.exceptions.NotFoundException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.commons.turtle.TurtleGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class IssueHandler {
    private final TurtleGenerator idGenerator = TurtleGenerator.get("Bruno");
    private final Bruno bruno;

    private static final ExceptionalFunction<JsonElement, IssueState, ? extends Exception> STATE_MAPPER = json -> IssueState.valueOf(json.getAsString());

    public IssueHandler(@NotNull Bruno bruno) {
        this.bruno = bruno;
    }

    public void onGetAll(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long   boardId    = request.getPathSegmentAsLong(1);

        final JsonArray responseBody = new JsonArray();
        final Set<Long> issueIds;
        try {
            issueIds = getDatabase().getIssueIds(boardId);
        } catch (NoSuchEntryException e) {
            throw new NotFoundException();
        }

        for (Long issueId : issueIds)
            responseBody.add(issueId);

        response.setStatus(Status.OK);
        response.setBody(responseBody);
    }

    public void onGet(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long boardId = request.getPathSegmentAsLong(1);
        final long issueId = request.getPathSegmentAsLong(3);

        try {
            final JsonObject responseBody = getDatabase().getIssue(boardId, issueId);

            response.setStatus(Status.OK);
            response.setBody(responseBody);
        } catch (NoSuchEntryException e) {
            response.setStatus(Status.NOT_FOUND);
        }
    }

    public void onCreate(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long boardId = request.getPathSegmentAsLong(1);

        final long       author = request.bodyHelper().requireElementAsLong("author");
        final String     title  = request.bodyHelper().requireElementAsString("title");
        final IssueState state  = request.bodyHelper().requireElement("state", STATE_MAPPER);

        final long id = idGenerator.newId();
        getDatabase().createIssue(boardId, id, author, title, state);

        final JsonObject responseBody = getDatabase().getBoard(id);

        response.setStatus(Status.CREATED);
        response.setBody(responseBody);
    }

    public void onAddAssignee(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long boardId = request.getPathSegmentAsLong(1);
        final long issueId = request.getPathSegmentAsLong(3);
        final long  userId = request.getPathSegmentAsLong(5);

        try {
            getDatabase().addIssueAssignee(boardId, issueId, userId);
        } catch (NoSuchEntryException e) {
            throw new NotFoundException();
        }

        response.setStatus(Status.NO_CONTENT);
    }

    public void onDelAssignee(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long boardId = request.getPathSegmentAsLong(1);
        final long issueId = request.getPathSegmentAsLong(3);
        final long  userId = request.getPathSegmentAsLong(5);

        try {
            getDatabase().removeIssueAssignee(boardId, issueId, userId);
        } catch (NoSuchEntryException e) {
            throw new NotFoundException();
        }

        response.setStatus(Status.NO_CONTENT);
    }

    public void onAddTag(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long boardId = request.getPathSegmentAsLong(1);
        final long issueId = request.getPathSegmentAsLong(3);
        final long   tagId = request.getPathSegmentAsLong(5);

        try {
            getDatabase().addIssueTag(boardId, issueId, tagId);
        } catch (NoSuchEntryException e) {
            throw new NotFoundException();
        }

        response.setStatus(Status.NO_CONTENT);
    }

    public void onDelTag(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long boardId = request.getPathSegmentAsLong(1);
        final long issueId = request.getPathSegmentAsLong(3);
        final long   tagId = request.getPathSegmentAsLong(5);

        try {
            getDatabase().removeIssueTag(boardId, issueId, tagId);
        } catch (NoSuchEntryException e) {
            throw new NotFoundException();
        }

        response.setStatus(Status.NO_CONTENT);
    }

    public void onDelete(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long boardId = request.getPathSegmentAsLong(1);
        final long issueId = request.getPathSegmentAsLong(3);

        getDatabase().deleteIssue(boardId, issueId);

        response.setStatus(Status.NO_CONTENT);
    }

    public void onEdit(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long boardId = request.getPathSegmentAsLong(1);
        final long issueId = request.getPathSegmentAsLong(3);

        request.bodyHelper().optionalElementAsString("title").ifPresent(title -> {
            getDatabase().updateIssueTitle(boardId, issueId, title);
        });
        request.bodyHelper().optionalElement("state", STATE_MAPPER).ifPresent(state -> {
            getDatabase().updateIssueState(boardId, issueId, state);
        });

        final JsonObject responseBody = getDatabase().getBoard(boardId);

        response.setStatus(Status.OK);
        response.setBody(responseBody);
    }

    private @NotNull Database getDatabase() {
        return this.bruno.getDatabase();
    }
}
