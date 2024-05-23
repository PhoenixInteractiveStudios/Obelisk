package org.burrow_studios.obelisk.monolith.http.handlers;

import org.burrow_studios.obelisk.core.entities.AbstractProject;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.http.Request;
import org.burrow_studios.obelisk.monolith.http.Response;
import org.burrow_studios.obelisk.monolith.http.exceptions.NotFoundException;
import org.burrow_studios.obelisk.monolith.http.exceptions.RequestHandlerException;
import org.jetbrains.annotations.NotNull;

public class ProjectHandler {
    private final ObeliskMonolith obelisk;

    public ProjectHandler(@NotNull ObeliskMonolith obelisk) {
        this.obelisk = obelisk;
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
}
