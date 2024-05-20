package org.burrow_studios.obelisk.monolith.entities;

import org.burrow_studios.obelisk.core.entities.AbstractDiscordAccount;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.entity.discord.DatabaseDiscordAccountDeleteAction;
import org.burrow_studios.obelisk.monolith.action.entity.discord.DatabaseDiscordAccountModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BackendDiscordAccount extends AbstractDiscordAccount {
    public BackendDiscordAccount(
            @NotNull ObeliskMonolith obelisk,
            long id,
            long snowflake,
            @NotNull String cachedName,
            @Nullable AbstractUser user
    ) {
        super(obelisk, id, snowflake, cachedName, user);
    }

    @Override
    public @NotNull DatabaseDiscordAccountModifier modify() {
        return new DatabaseDiscordAccountModifier(this);
    }

    @Override
    public @NotNull DatabaseDiscordAccountDeleteAction delete() {
        return new DatabaseDiscordAccountDeleteAction(this);
    }
}
