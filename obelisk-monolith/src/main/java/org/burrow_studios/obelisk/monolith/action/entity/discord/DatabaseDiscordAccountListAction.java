package org.burrow_studios.obelisk.monolith.action.entity.discord;

import org.burrow_studios.obelisk.core.cache.EntityCache;
import org.burrow_studios.obelisk.core.entities.AbstractDiscordAccount;
import org.burrow_studios.obelisk.monolith.action.DatabaseListAction;
import org.burrow_studios.obelisk.monolith.db.interfaces.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendDiscordAccount;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DatabaseDiscordAccountListAction extends DatabaseListAction<AbstractDiscordAccount> {
    public DatabaseDiscordAccountListAction(@NotNull EntityCache<AbstractDiscordAccount> cache) {
        super(cache);
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<EntityCache<AbstractDiscordAccount>> future) throws DatabaseException {
        List<BackendDiscordAccount> discordAccounts = actionableDatabase.getDiscordAccounts(this);

        this.getCache().clear();
        for (BackendDiscordAccount discordAccount : discordAccounts)
            this.getCache().add(discordAccount);

        future.complete(this.getCache());
    }
}
