package org.burrow_studios.obelisk.monolith.action.entity.discord;

import org.burrow_studios.obelisk.api.entities.DiscordAccount;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseGetAction;
import org.jetbrains.annotations.NotNull;

public class DatabaseDiscordAccountGetAction extends DatabaseGetAction<DiscordAccount> {
    public DatabaseDiscordAccountGetAction(@NotNull ObeliskMonolith obelisk, long id) {
        super(obelisk, id);
    }
}
