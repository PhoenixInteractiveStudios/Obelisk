package org.burrow_studios.obelisk.monolith.action.entity.discord;

import org.burrow_studios.obelisk.api.entities.DiscordAccount;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseDeleteAction;
import org.burrow_studios.obelisk.monolith.db.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendDiscordAccount;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DatabaseDiscordAccountDeleteAction extends DatabaseDeleteAction<DiscordAccount> {
    public DatabaseDiscordAccountDeleteAction(@NotNull BackendDiscordAccount discordAccount) {
        super(((ObeliskMonolith) discordAccount.getAPI()), discordAccount.getId(), DiscordAccount.class);
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<Void> future) throws DatabaseException {
        actionableDatabase.deleteDiscordAccount(this);
        future.complete(null);
    }
}
