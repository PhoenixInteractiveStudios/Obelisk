package org.burrow_studios.obelisk.monolith.http.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.core.entities.AbstractProject;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.entity.project.DatabaseProjectBuilder;
import org.burrow_studios.obelisk.monolith.entities.BackendProject;
import org.burrow_studios.obelisk.monolith.http.Request;
import org.burrow_studios.obelisk.monolith.http.Response;
import org.burrow_studios.obelisk.monolith.http.exceptions.BadRequestException;
import org.burrow_studios.obelisk.monolith.http.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.monolith.http.exceptions.NotFoundException;
import org.burrow_studios.obelisk.monolith.http.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.util.Pipe;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;

public class ProjectHandler {
    private final ObeliskMonolith obelisk;

    public ProjectHandler(@NotNull ObeliskMonolith obelisk) {
        this.obelisk = obelisk;
    }

    public @NotNull Response onList(@NotNull Request request) throws RequestHandlerException {
        JsonArray arr = new JsonArray();

        this.obelisk.getProjects().forEach(project -> arr.add(project.toMinimalJson()));

        return new Response.Builder()
                .setStatus(200)
                .setBody(arr)
                .build();
    }

    public @NotNull Response onGet(@NotNull Request request) throws RequestHandlerException {
        final long projectId = request.parsePathSegment(1, Long::parseLong);

        AbstractProject project = this.obelisk.getProject(projectId);

        if (project == null) {
            // TODO: query database?
        }

        if (project == null)
            throw new NotFoundException("Project not found");

        return new Response.Builder()
                .setBody(project.toJson())
                .setStatus(200)
                .build();
    }

    public @NotNull Response onPost(@NotNull Request request) throws RequestHandlerException {
        JsonObject requestJson = request.requireBodyObject();

        DatabaseProjectBuilder builder = this.obelisk.createProject();

        String title = Pipe.of(requestJson.get("title"), BadRequestException::new)
                .nonNull("Missing \"title\" attribute")
                .map(JsonElement::getAsString, "Malformed \"title\" attribute")
                .get();
        builder.setTitle(title);

        Project.State state = Pipe.of(requestJson.get("state"), BadRequestException::new)
                .nonNull("Missing \"state\" attribute")
                .map(JsonElement::getAsString, "Malformed \"state\" attribute")
                .map(Project.State::valueOf, "Malformed \"state\" attribute")
                .get();
        builder.setState(state);

        BackendProject project;
        try {
            project = ((BackendProject) builder.await());
        } catch (ExecutionException | InterruptedException e) {
            throw new InternalServerErrorException();
        }

        return new Response.Builder()
                .setBody(project.toJson())
                .setStatus(201)
                .build();
    }
}
