package org.burrow_studios.obelisk.monolith.action.entity.project;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseAction;
import org.burrow_studios.obelisk.monolith.db.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendProject;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DatabaseProjectUserRemoveAction extends DatabaseAction<Void> {
    private final Project project;
    private final User user;

    public DatabaseProjectUserRemoveAction(@NotNull BackendProject project, @NotNull User user) {
        super(((ObeliskMonolith) project.getAPI()));
        this.project = project;
        this.user = user;
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<Void> future) throws DatabaseException {
        actionableDatabase.removeProjectMember(this);
        future.complete(null);
    }

    public @NotNull Project getProject() {
        return this.project;
    }

    public @NotNull User getUser() {
        return this.user;
    }
}
