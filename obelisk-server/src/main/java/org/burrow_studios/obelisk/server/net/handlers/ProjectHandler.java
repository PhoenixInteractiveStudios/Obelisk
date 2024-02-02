package org.burrow_studios.obelisk.server.net.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.commons.http.server.Request;
import org.burrow_studios.obelisk.commons.http.server.ResponseBuilder;
import org.burrow_studios.obelisk.commons.http.server.exceptions.BadRequestException;
import org.burrow_studios.obelisk.commons.http.server.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.commons.http.server.exceptions.NotFoundException;
import org.burrow_studios.obelisk.commons.http.server.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.entities.action.project.ProjectBuilderImpl;
import org.burrow_studios.obelisk.core.entities.action.project.ProjectModifierImpl;
import org.burrow_studios.obelisk.core.entities.action.project.ProjectUtils;
import org.burrow_studios.obelisk.core.entities.impl.ProjectImpl;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.stream.StreamSupport;

public class ProjectHandler {
    private final @NotNull NetworkHandler networkHandler;

    public ProjectHandler(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void onGetAll(@NotNull Request request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        JsonArray json = new JsonArray();
        for (long id : getAPI().getProjects().getIdsAsImmutaleSet())
            json.add(id);

        response.setCode(200);
        response.setBody(json);
    }

    public void onGet(@NotNull Request request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        final long projectId = request.getSegmentLong(1);

        final ProjectImpl project = getAPI().getProject(projectId);
        if (project == null)
            throw new NotFoundException();

        response.setCode(200);
        response.setBody(project.toJson());
    }

    public void onCreate(@NotNull Request request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        Optional<JsonObject> body = request.optionalBody()
                .map(JsonElement::getAsJsonObject);

        if (body.isEmpty())
            throw new BadRequestException("No content");

        final JsonObject json = body.get();
        final JsonObject result;

        final ProjectBuilderImpl builder;

        try {
            final String title = json.get("title").getAsString();

            final String        stateStr = json.get("state").getAsString();
            final Project.State state    = Project.State.valueOf(stateStr);

            final JsonObject  jsonTimings = json.getAsJsonObject("timings");
            final Project.Timings timings = ProjectUtils.buildProjectTimings(jsonTimings);

            builder = getAPI().createProject()
                    .setTitle(title)
                    .setState(state)
                    .setTimings(timings);

            JsonArray members = json.getAsJsonArray("members");
            StreamSupport.stream(members.spliterator(), false)
                    .map(JsonElement::getAsLong)
                    .map(id -> {
                        UserImpl user = getAPI().getUser(id);
                        if (user == null)
                            throw new IllegalArgumentException("Invalid user id: " + id);
                        return user;
                    })
                    .forEach(builder::addMembers);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Malformed body");
        }

        try {
            result = ((ProjectImpl) builder.await()).toJson();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(result);
    }

    public void onAddMember(@NotNull Request request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        final long projectId = request.getSegmentLong(1);
        final long    userId = request.getSegmentLong(3);

        final ProjectImpl project = getAPI().getProject(projectId);
        final UserImpl    user    = getAPI().getUser(userId);

        if (project == null || user == null)
            throw new NotFoundException();

        try {
            project.addMember(user).await();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        response.setCode(204);
    }

    public void onDeleteMember(@NotNull Request request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        final long projectId = request.getSegmentLong(1);
        final long    userId = request.getSegmentLong(3);

        final ProjectImpl project = getAPI().getProject(projectId);
        final UserImpl    user    = getAPI().getUser(userId);

        if (project == null)
            throw new NotFoundException();

        if (user != null && project.getMembers().containsId(userId)) {
            try {
                project.removeMember(user).await();
            } catch (Exception e) {
                throw new InternalServerErrorException();
            }
        }

        response.setCode(204);
    }

    public void onDelete(@NotNull Request request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        final long projectId = request.getSegmentLong(1);
        final ProjectImpl project = getAPI().getProject(projectId);

        if (project != null) {
            try {
                project.delete().await();
            } catch (Exception e) {
                throw new InternalServerErrorException();
            }
        }

        response.setCode(204);
    }

    public void onEdit(@NotNull Request request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        final long projectId = request.getSegmentLong(1);

        Optional<JsonObject> body = request.optionalBody()
                .map(JsonElement::getAsJsonObject);

        if (body.isEmpty())
            throw new BadRequestException("No content");

        final JsonObject json = body.get();

        ProjectImpl project = getAPI().getProject(projectId);
        if (project == null)
            throw new NotFoundException();
        final ProjectModifierImpl modifier = project.modify();

        try {
            Optional.ofNullable(json.get("title"))
                    .map(JsonElement::getAsString)
                    .ifPresent(modifier::setTitle);

            Optional.ofNullable(json.get("state"))
                    .map(JsonElement::getAsString)
                    .map(Project.State::valueOf)
                    .ifPresent(modifier::setState);

            Optional.ofNullable(json.get("timings"))
                    .map(JsonElement::getAsJsonObject)
                    .map(ProjectUtils::buildProjectTimings)
                    .ifPresent(modifier::setTimings);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Malformed body");
        }

        try {
            project = ((ProjectImpl) modifier.await());
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(project.toJson());
    }

    private @NotNull ObeliskImpl getAPI() {
        return this.networkHandler.getServer().getAPI();
    }
}
