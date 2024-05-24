package org.burrow_studios.obelisk.monolith.action.entity.project;

import org.burrow_studios.obelisk.core.cache.EntityCache;
import org.burrow_studios.obelisk.core.entities.AbstractProject;
import org.burrow_studios.obelisk.monolith.action.DatabaseListAction;
import org.burrow_studios.obelisk.monolith.db.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendProject;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DatabaseProjectListAction extends DatabaseListAction<AbstractProject> {
    public DatabaseProjectListAction(@NotNull EntityCache<AbstractProject> cache) {
        super(cache);
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<EntityCache<AbstractProject>> future) throws DatabaseException {
        List<BackendProject> projects = actionableDatabase.onProjectList(this);

        this.getCache().clear();
        for (BackendProject project : projects)
            this.getCache().add(project);

        future.complete(this.getCache());
    }
}
