package org.burrow_studios.obelisk.monolith.action.entity.user;

import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseDeleteAction;
import org.burrow_studios.obelisk.monolith.entities.BackendUser;
import org.jetbrains.annotations.NotNull;

public class DatabaseUserDeleteAction extends DatabaseDeleteAction<User> {
    public DatabaseUserDeleteAction(@NotNull BackendUser user) {
        super(((ObeliskMonolith) user.getAPI()), user.getId(), User.class);
    }
}
