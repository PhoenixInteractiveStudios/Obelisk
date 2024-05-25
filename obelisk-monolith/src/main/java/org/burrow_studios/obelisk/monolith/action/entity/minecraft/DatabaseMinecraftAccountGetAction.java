package org.burrow_studios.obelisk.monolith.action.entity.minecraft;

import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseGetAction;
import org.burrow_studios.obelisk.monolith.db.interfaces.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendMinecraftAccount;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DatabaseMinecraftAccountGetAction extends DatabaseGetAction<BackendMinecraftAccount> {
    public DatabaseMinecraftAccountGetAction(@NotNull ObeliskMonolith obelisk, long id) {
        super(obelisk, id);
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<BackendMinecraftAccount> future) throws DatabaseException {
        BackendMinecraftAccount minecraftAccount = actionableDatabase.getMinecraftAccount(this);
        future.complete(minecraftAccount);
    }
}
