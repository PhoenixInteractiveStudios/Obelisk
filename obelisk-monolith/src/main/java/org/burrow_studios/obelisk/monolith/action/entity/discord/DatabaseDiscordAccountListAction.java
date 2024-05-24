package org.burrow_studios.obelisk.monolith.action.entity.discord;

import org.burrow_studios.obelisk.core.cache.EntityCache;
import org.burrow_studios.obelisk.core.entities.AbstractDiscordAccount;
import org.burrow_studios.obelisk.monolith.action.DatabaseListAction;
import org.jetbrains.annotations.NotNull;

public class DatabaseDiscordAccountListAction extends DatabaseListAction<AbstractDiscordAccount> {
    public DatabaseDiscordAccountListAction(@NotNull EntityCache<AbstractDiscordAccount> cache) {
        super(cache);
    }
}
