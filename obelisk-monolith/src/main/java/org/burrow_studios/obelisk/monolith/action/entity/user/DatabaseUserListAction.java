package org.burrow_studios.obelisk.monolith.action.entity.user;

import org.burrow_studios.obelisk.core.cache.EntityCache;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.monolith.action.DatabaseListAction;
import org.jetbrains.annotations.NotNull;

public class DatabaseUserListAction extends DatabaseListAction<AbstractUser> {
    public DatabaseUserListAction(@NotNull EntityCache<AbstractUser> cache) {
        super(cache);
    }
}
