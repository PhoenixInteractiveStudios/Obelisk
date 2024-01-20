package org.burrow_studios.obelisk.server.db.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.action.project.ProjectUtils;
import org.burrow_studios.obelisk.core.entities.impl.ProjectImpl;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.burrow_studios.obelisk.core.source.Request;
import org.burrow_studios.obelisk.core.source.Response;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.EntityProvider;
import org.burrow_studios.obelisk.server.db.NoSuchEntryException;
import org.burrow_studios.obelisk.server.db.RequestHandler;
import org.burrow_studios.obelisk.server.db.entity.ProjectDB;
import org.burrow_studios.obelisk.util.TurtleGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public class ProjectHandler implements RequestHandler {
    private final TurtleGenerator turtleGenerator;
    private final EntityProvider provider;

    public ProjectHandler(@NotNull EntityProvider provider) {
        this.provider = provider;
        this.turtleGenerator = TurtleGenerator.get(provider.getClass().getSimpleName());
    }

    public @NotNull Response onGetAll(@NotNull Request request) {
        final JsonArray projects = new JsonArray();
        final Set<Long> projectIds;

        try {
            projectIds = this.getDB().getProjectIds();
        } catch (DatabaseException e) {
            return request.respond(500, null);
        }

        for (long projectId : projectIds)
            projects.add(projectId);

        return request.respond(200, projects);
    }

    public @NotNull Response onGet(@NotNull Request request) {
        final long projectId = (long) request.getEndpoint().args()[1];
        final JsonObject project;

        try {
            project = this.getDB().getProject(projectId);
        } catch (NoSuchEntryException e) {
            return request.respond(404, null);
        } catch (DatabaseException e) {
            return request.respond(500, null);
        }

        return request.respond(200, project);
    }

    public @NotNull Response onCreate(@NotNull Request request) {
        final long id = this.turtleGenerator.newId();

        final JsonObject json = request.optionalContent()
                .map(JsonElement::getAsJsonObject)
                .orElse(null);

        if (json == null)
            return request.respond(400, null);

        json.addProperty("id", id);

        final ProjectImpl project = new ProjectImpl(provider.getAPI(), new EntityData(json));

        this.getDB().createProject(
                project.getId(),
                project.getTitle(),
                project.getState()
        );

        for (UserImpl member : project.getMembers())
            this.getDB().addProjectMember(project.getId(), member.getId());

        final Project.Timings timings = project.getTimings();
        this.applyTimings(project.getId(), timings);

        return request.respond(200, project.toJson());
    }

    public @NotNull Response onAddMember(@NotNull Request request) {
        final long projectId = (long) request.getEndpoint().args()[1];
        final long    userId = (long) request.getEndpoint().args()[3];

        this.getDB().addProjectMember(projectId, userId);

        return request.respond(204, null);
    }

    public @NotNull Response onDeleteMember(@NotNull Request request) {
        final long projectId = (long) request.getEndpoint().args()[1];
        final long    userId = (long) request.getEndpoint().args()[3];

        this.getDB().removeProjectMember(projectId, userId);

        return request.respond(204, null);
    }

    public @NotNull Response onDelete(@NotNull Request request) {
        final long projectId = (long) request.getEndpoint().args()[1];

        this.getDB().deleteProject(projectId);

        return request.respond(204, null);
    }

    public @NotNull Response onEdit(@NotNull Request request) {
        final long projectId = (long) request.getEndpoint().args()[1];
        final JsonObject json = request.optionalContent()
                .map(JsonElement::getAsJsonObject)
                .orElse(null);

        if (json == null)
            return request.respond(400, null);

        Optional.ofNullable(json.get("title"))
                .map(JsonElement::getAsString)
                .ifPresent(title -> getDB().updateProjectTitle(projectId, title));

        Optional.ofNullable(json.get("state"))
                .map(JsonElement::getAsString)
                .map(Project.State::valueOf)
                .ifPresent(state -> getDB().updateProjectState(projectId, state));

        Optional.ofNullable(json.get("timings"))
                .map(JsonElement::getAsJsonObject)
                .map(ProjectUtils::buildProjectTimings)
                .ifPresent(timings -> applyTimings(projectId, timings));

        final JsonObject project = getDB().getProject(projectId);
        return request.respond(200, project);
    }

    private void applyTimings(long projectId, @NotNull Project.Timings timings) {
        if (timings.release() != null)
            this.getDB().addProjectTiming(projectId, "release", timings.release());
        if (timings.apply() != null)
            this.getDB().addProjectTiming(projectId, "apply", timings.apply());
        if (timings.start() != null)
            this.getDB().addProjectTiming(projectId, "start", timings.start());
        if (timings.end() != null)
            this.getDB().addProjectTiming(projectId, "end", timings.end());
    }

    private @NotNull ProjectDB getDB() {
        return this.provider.getProjectDB();
    }
}
