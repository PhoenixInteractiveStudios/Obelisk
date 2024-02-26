package org.burrow_studios.obelisk.moderationservice.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.function.ExceptionalFunction;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.Status;
import org.burrow_studios.obelisk.commons.rpc.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.commons.turtle.TurtleGenerator;
import org.burrow_studios.obelisk.moderationservice.ModerationService;
import org.burrow_studios.obelisk.moderationservice.database.ProjectDB;
import org.burrow_studios.obelisk.moderationservice.database.ProjectState;
import org.burrow_studios.obelisk.moderationservice.exceptions.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ProjectHandler {
    private final TurtleGenerator idGenerator = TurtleGenerator.get("ModerationService");
    private final ModerationService service;

    private static final ExceptionalFunction<JsonElement, ProjectState, ? extends Exception> STATE_MAPPER = json -> ProjectState.valueOf(json.getAsString());

    public ProjectHandler(@NotNull ModerationService service) {
        this.service = service;
    }

    public void onGetAll(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final JsonArray responseBody = new JsonArray();
        final Set<Long> projectIds = getDatabase().getProjectIds();

        for (Long projectId : projectIds)
            responseBody.add(projectId);

        response.setStatus(Status.OK);
        response.setBody(responseBody);
    }

    public void onGet(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long projectId = request.getPathSegmentAsLong(1);

        try {
            final JsonObject responseBody = getDatabase().getProject(projectId);

            response.setStatus(Status.OK);
            response.setBody(responseBody);
        } catch (NoSuchEntryException e) {
            response.setStatus(Status.NOT_FOUND);
        }
    }

    public void onCreate(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final String       title = request.bodyHelper().requireElementAsString("title");
        final ProjectState state = request.bodyHelper().requireElement("state", STATE_MAPPER);

        final long id = idGenerator.newId();
        getDatabase().createProject(id, title, state);

        final JsonObject responseBody = getDatabase().getProject(id);

        response.setStatus(Status.CREATED);
        response.setBody(responseBody);
    }

    public void onAddMember(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long projectId = request.getPathSegmentAsLong(1);
        final long    userId = request.getPathSegmentAsLong(3);

        getDatabase().addProjectMember(projectId, userId);

        response.setStatus(Status.NO_CONTENT);
    }

    public void onDelMember(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long projectId = request.getPathSegmentAsLong(1);
        final long    userId = request.getPathSegmentAsLong(3);

        getDatabase().removeProjectMember(projectId, userId);

        response.setStatus(Status.NO_CONTENT);
    }

    public void onDelete(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long projectId = request.getPathSegmentAsLong(1);

        getDatabase().deleteProject(projectId);

        response.setStatus(Status.NO_CONTENT);
    }

    public void onEdit(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long projectId = request.getPathSegmentAsLong(1);

        request.bodyHelper().optionalElementAsString("title").ifPresent(title -> {
            getDatabase().updateProjectTitle(projectId, title);
        });
        request.bodyHelper().optionalElement("state", STATE_MAPPER).ifPresent(state -> {
            getDatabase().updateProjectState(projectId, state);
        });

        final JsonObject responseBody = getDatabase().getProject(projectId);

        response.setStatus(Status.OK);
        response.setBody(responseBody);
    }

    private @NotNull ProjectDB getDatabase() {
        return this.service.getProjectDB();
    }
}
