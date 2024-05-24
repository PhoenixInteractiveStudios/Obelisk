package org.burrow_studios.obelisk.monolith.action.entity.minecraft;

import org.burrow_studios.obelisk.core.cache.EntityCache;
import org.burrow_studios.obelisk.core.entities.AbstractMinecraftAccount;
import org.burrow_studios.obelisk.monolith.action.DatabaseListAction;
import org.burrow_studios.obelisk.monolith.db.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendMinecraftAccount;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DatabaseMinecraftAccountListAction extends DatabaseListAction<AbstractMinecraftAccount> {
    public DatabaseMinecraftAccountListAction(@NotNull EntityCache<AbstractMinecraftAccount> cache) {
        super(cache);
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<EntityCache<AbstractMinecraftAccount>> future) throws DatabaseException {
        List<BackendMinecraftAccount> minecraftAccounts = actionableDatabase.getMinecraftAccounts(this);

        this.getCache().clear();
        for (BackendMinecraftAccount minecraftAccount : minecraftAccounts)
            this.getCache().add(minecraftAccount);

        future.complete(this.getCache());
    }
}
