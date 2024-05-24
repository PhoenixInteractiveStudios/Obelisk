package org.burrow_studios.obelisk.monolith.action.entity.project;

import org.burrow_studios.obelisk.core.cache.EntityCache;
import org.burrow_studios.obelisk.core.entities.AbstractProject;
import org.burrow_studios.obelisk.monolith.action.DatabaseListAction;
import org.jetbrains.annotations.NotNull;

public class DatabaseProjectListAction extends DatabaseListAction<AbstractProject> {
    public DatabaseProjectListAction(@NotNull EntityCache<AbstractProject> cache) {
        super(cache);
    }
}
