package org.burrow_studios.obelisk.monolith.action.entity.minecraft;

import org.burrow_studios.obelisk.api.entities.MinecraftAccount;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseDeleteAction;
import org.burrow_studios.obelisk.monolith.db.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendMinecraftAccount;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DatabaseMinecraftAccountDeleteAction extends DatabaseDeleteAction<MinecraftAccount> {
    public DatabaseMinecraftAccountDeleteAction(@NotNull BackendMinecraftAccount minecraftAccount) {
        super(((ObeliskMonolith) minecraftAccount.getAPI()), minecraftAccount.getId(), MinecraftAccount.class);
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<Void> future) throws DatabaseException {
        actionableDatabase.onMinecraftAccountDelete(this);
        future.complete(null);
    }
}
