package org.burrow_studios.obelisk.monolith.action.entity.user;

import org.burrow_studios.obelisk.core.cache.EntityCache;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.monolith.action.DatabaseListAction;
import org.burrow_studios.obelisk.monolith.db.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendUser;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DatabaseUserListAction extends DatabaseListAction<AbstractUser> {
    public DatabaseUserListAction(@NotNull EntityCache<AbstractUser> cache) {
        super(cache);
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<EntityCache<AbstractUser>> future) throws DatabaseException {
        List<BackendUser> users = actionableDatabase.getUsers(this);

        this.getCache().clear();
        for (BackendUser user : users)
            this.getCache().add(user);

        future.complete(this.getCache());
    }
}
