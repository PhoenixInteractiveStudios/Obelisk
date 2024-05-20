package org.burrow_studios.obelisk.monolith.action.entity.project;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseGetAction;
import org.jetbrains.annotations.NotNull;

public class DatabaseProjectGetAction extends DatabaseGetAction<Project> {
    public DatabaseProjectGetAction(@NotNull ObeliskMonolith obelisk, long id) {
        super(obelisk, id);
    }
}
