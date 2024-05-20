package org.burrow_studios.obelisk.monolith.entities;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.discord.MinecraftAccountModifier;
import org.burrow_studios.obelisk.api.entities.MinecraftAccount;
import org.burrow_studios.obelisk.core.entities.AbstractMinecraftAccount;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BackendMinecraftAccount extends AbstractMinecraftAccount {
    public BackendMinecraftAccount(
            @NotNull ObeliskMonolith obelisk,
            long id,
            @NotNull UUID uuid,
            @NotNull String cachedName,
            @Nullable AbstractUser user
    ) {
        super(obelisk, id, uuid, cachedName, user);
    }

    @Override
    public @NotNull MinecraftAccountModifier modify() {
        // TODO
        return null;
    }

    @Override
    public @NotNull DeleteAction<MinecraftAccount> delete() {
        // TODO
        return null;
    }
}
