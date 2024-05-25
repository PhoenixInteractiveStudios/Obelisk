package org.burrow_studios.obelisk.monolith.action.entity.user;

import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseDeleteAction;
import org.burrow_studios.obelisk.monolith.db.interfaces.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendUser;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DatabaseUserDeleteAction extends DatabaseDeleteAction<User> {
    public DatabaseUserDeleteAction(@NotNull BackendUser user) {
        super(((ObeliskMonolith) user.getAPI()), user.getId(), User.class);
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<Void> future) throws DatabaseException {
        actionableDatabase.deleteUser(this);
        future.complete(null);
    }
}
