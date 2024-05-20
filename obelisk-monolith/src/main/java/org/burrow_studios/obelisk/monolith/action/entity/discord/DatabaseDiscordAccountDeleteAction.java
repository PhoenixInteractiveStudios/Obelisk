package org.burrow_studios.obelisk.monolith.action.entity.discord;

import org.burrow_studios.obelisk.api.entities.DiscordAccount;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseDeleteAction;
import org.burrow_studios.obelisk.monolith.entities.BackendDiscordAccount;
import org.jetbrains.annotations.NotNull;

public class DatabaseDiscordAccountDeleteAction extends DatabaseDeleteAction<DiscordAccount> {
    public DatabaseDiscordAccountDeleteAction(@NotNull BackendDiscordAccount discordAccount) {
        super(((ObeliskMonolith) discordAccount.getAPI()), discordAccount.getId(), DiscordAccount.class);
    }
}
