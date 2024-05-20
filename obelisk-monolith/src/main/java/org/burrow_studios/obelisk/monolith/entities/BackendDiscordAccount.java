package org.burrow_studios.obelisk.monolith.entities;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.minecraft.DiscordAccountModifier;
import org.burrow_studios.obelisk.api.entities.DiscordAccount;
import org.burrow_studios.obelisk.core.entities.AbstractDiscordAccount;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
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
    public @NotNull DiscordAccountModifier modify() {
        // TODO
        return null;
    }

    @Override
    public @NotNull DeleteAction<DiscordAccount> delete() {
        // TODO
        return null;
    }
}
