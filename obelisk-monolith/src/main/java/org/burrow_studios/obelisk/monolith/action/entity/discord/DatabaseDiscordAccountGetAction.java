package org.burrow_studios.obelisk.monolith.action.entity.discord;

import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseGetAction;
import org.burrow_studios.obelisk.monolith.db.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendDiscordAccount;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DatabaseDiscordAccountGetAction extends DatabaseGetAction<BackendDiscordAccount> {
    public DatabaseDiscordAccountGetAction(@NotNull ObeliskMonolith obelisk, long id) {
        super(obelisk, id);
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<BackendDiscordAccount> future) throws DatabaseException {
        BackendDiscordAccount discordAccount = actionableDatabase.onDiscordAccountGet(this);
        future.complete(discordAccount);
    }
}
