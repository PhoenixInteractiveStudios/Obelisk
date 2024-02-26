package org.burrow_studios.obelisk.bruno.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.bruno.Bruno;
import org.burrow_studios.obelisk.bruno.database.Database;
import org.burrow_studios.obelisk.bruno.database.IssueState;
import org.burrow_studios.obelisk.bruno.exceptions.NoSuchEntryException;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.Status;
import org.burrow_studios.obelisk.commons.rpc.exceptions.BadRequestException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.NotFoundException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.commons.turtle.TurtleGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class IssueHandler {
    private final TurtleGenerator idGenerator = TurtleGenerator.get("Bruno");
    private final Bruno bruno;

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

        if (!(request.getBody() instanceof JsonObject requestBody))
            throw new BadRequestException("Missing request body");

        if (!(requestBody.get("author") instanceof JsonPrimitive authorInfo) || !authorInfo.isNumber())
            throw new BadRequestException("Malformed request body: Malformed author info");
        final long author = authorInfo.getAsLong();

        if (!(requestBody.get("title") instanceof JsonPrimitive titleInfo))
            throw new BadRequestException("Malformed request body: Malformed title info");
        final String title = titleInfo.getAsString();

        if (!(requestBody.get("state") instanceof JsonPrimitive stateInfo))
            throw new BadRequestException("Malformed request body: Malformed state info");
        final IssueState state;
        try {
            state = IssueState.valueOf(stateInfo.getAsString());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Malformed request body: Illegal state value");
        }

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

        if (!(request.getBody() instanceof JsonObject requestBody))
            throw new BadRequestException("Missing request body");

        final JsonElement titleInfo = requestBody.get("title");
        if (titleInfo != null) {
            if (!(titleInfo instanceof JsonPrimitive json))
                throw new BadRequestException("Malformed request body: Malformed title info");

            final String title = json.getAsString();

            getDatabase().updateIssueTitle(boardId, issueId, title);
        }

        final JsonElement stateInfo = requestBody.get("state");
        if (stateInfo != null) {
            if (!(stateInfo instanceof JsonPrimitive json))
                throw new BadRequestException("Malformed request body: Malformed state info");

            final IssueState state;
            try {
                state = IssueState.valueOf(json.getAsString());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Malformed request body: Illegal state value");
            }

            getDatabase().updateIssueState(boardId, issueId, state);
        }

        final JsonObject responseBody = getDatabase().getBoard(boardId);

        response.setStatus(Status.OK);
        response.setBody(responseBody);
    }

    private @NotNull Database getDatabase() {
        return this.bruno.getDatabase();
    }
}
