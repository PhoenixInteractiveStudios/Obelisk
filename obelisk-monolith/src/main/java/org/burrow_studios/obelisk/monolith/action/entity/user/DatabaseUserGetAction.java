package org.burrow_studios.obelisk.monolith.action.entity.user;

import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseGetAction;
import org.jetbrains.annotations.NotNull;

public class DatabaseUserGetAction extends DatabaseGetAction<User> {
    public DatabaseUserGetAction(@NotNull ObeliskMonolith obelisk, long id) {
        super(obelisk, id);
    }
}
