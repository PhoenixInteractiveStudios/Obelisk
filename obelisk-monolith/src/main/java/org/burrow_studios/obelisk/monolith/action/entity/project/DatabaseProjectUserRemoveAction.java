package org.burrow_studios.obelisk.monolith.action.entity.project;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseAction;
import org.burrow_studios.obelisk.monolith.entities.BackendProject;
import org.jetbrains.annotations.NotNull;

public class DatabaseProjectUserRemoveAction extends DatabaseAction<Void> {
    private final Project project;
    private final User user;

    public DatabaseProjectUserRemoveAction(@NotNull BackendProject project, @NotNull User user) {
        super(((ObeliskMonolith) project.getAPI()));
        this.project = project;
        this.user = user;
    }

    public @NotNull Project getProject() {
        return this.project;
    }

    public @NotNull User getUser() {
        return this.user;
    }
}
