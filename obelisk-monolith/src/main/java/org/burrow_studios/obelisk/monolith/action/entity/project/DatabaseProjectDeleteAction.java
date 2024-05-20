package org.burrow_studios.obelisk.monolith.action.entity.project;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseDeleteAction;
import org.burrow_studios.obelisk.monolith.entities.BackendProject;
import org.jetbrains.annotations.NotNull;

public class DatabaseProjectDeleteAction extends DatabaseDeleteAction<Project> {
    public DatabaseProjectDeleteAction(@NotNull BackendProject project) {
        super(((ObeliskMonolith) project.getAPI()), project.getId(), Project.class);
    }
}
