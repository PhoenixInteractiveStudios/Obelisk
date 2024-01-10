package org.burrow_studios.obelisk.server.net.http.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.NoSuchEntryException;
import org.burrow_studios.obelisk.server.moderation.ModerationService;
import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.burrow_studios.obelisk.server.net.http.Request;
import org.burrow_studios.obelisk.server.net.http.ResponseBuilder;
import org.burrow_studios.obelisk.server.net.http.exceptions.APIException;
import org.burrow_studios.obelisk.server.net.http.exceptions.BadRequestException;
import org.burrow_studios.obelisk.server.net.http.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.server.net.http.exceptions.NotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ProjectHandler {
    private final @NotNull NetworkHandler networkHandler;

    public ProjectHandler(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void onGetAll(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        JsonArray json = new JsonArray();
        getModerationService().getProjects().forEach(json::add);

        response.setCode(200);
        response.setBody(json);
    }

    public void onGet(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long projectId = request.getSegmentLong(1);

        final JsonObject project;

        try {
            project = getModerationService().getProject(projectId);
        } catch (NoSuchEntryException e) {
            throw new NotFoundException();
        } catch (DatabaseException e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(project);
    }

    public void onCreate(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        Optional<JsonObject> body = request.optionalBody()
                .map(JsonElement::getAsJsonObject);

        if (body.isEmpty())
            throw new BadRequestException("No content");

        final JsonObject json = body.get();
        final JsonObject result;

        try {
            result = getModerationService().createProject(json);
        } catch (DatabaseException e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(result);
    }

    public void onAddMember(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long project = request.getSegmentLong(1);
        final long user    = request.getSegmentLong(3);

        try {
            getModerationService().addProjectMember(project, user);
        } catch (DatabaseException e) {
            throw new InternalServerErrorException();
        }

        response.setCode(204);
    }

    public void onDeleteMember(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long project = request.getSegmentLong(1);
        final long user    = request.getSegmentLong(3);

        try {
            getModerationService().removeProjectMember(project, user);
        } catch (DatabaseException e) {
            throw new InternalServerErrorException();
        }

        response.setCode(204);
    }

    public void onDelete(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        response.setCode(501);
    }

    public void onEdit(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        long project = request.getSegmentLong(1);

        Optional<JsonObject> body = request.optionalBody()
                .map(JsonElement::getAsJsonObject);

        if (body.isEmpty())
            throw new BadRequestException("No content");

        final JsonObject json = body.get();
        final JsonObject result;

        try {
            getModerationService().patchProject(project, json);

            result = getModerationService().getProject(project);
        } catch (DatabaseException e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(result);
    }

    private @NotNull ModerationService getModerationService() {
        return this.networkHandler.getServer().getModerationService();
    }
}
