package org.burrow_studios.obelisk.monolith.action.entity.project;

import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseGetAction;
import org.burrow_studios.obelisk.monolith.db.interfaces.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendProject;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DatabaseProjectGetAction extends DatabaseGetAction<BackendProject> {
    public DatabaseProjectGetAction(@NotNull ObeliskMonolith obelisk, long id) {
        super(obelisk, id);
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<BackendProject> future) throws DatabaseException {
        BackendProject project = actionableDatabase.getProject(this);
        future.complete(project);
    }
}
