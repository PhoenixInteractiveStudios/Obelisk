package org.burrow_studios.obelisk.monolith.action.entity.user;

import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseGetAction;
import org.burrow_studios.obelisk.monolith.db.interfaces.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendUser;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DatabaseUserGetAction extends DatabaseGetAction<BackendUser> {
    public DatabaseUserGetAction(@NotNull ObeliskMonolith obelisk, long id) {
        super(obelisk, id);
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<BackendUser> future) throws DatabaseException {
        BackendUser user = actionableDatabase.getUser(this);
        future.complete(user);
    }
}
