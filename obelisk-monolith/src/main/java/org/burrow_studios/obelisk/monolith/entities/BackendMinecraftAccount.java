package org.burrow_studios.obelisk.monolith.entities;

import org.burrow_studios.obelisk.core.entities.AbstractMinecraftAccount;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.DatabaseMinecraftAccountDeleteAction;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.DatabaseMinecraftAccountModifier;
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
    public @NotNull DatabaseMinecraftAccountModifier modify() {
        return new DatabaseMinecraftAccountModifier(this);
    }

    @Override
    public @NotNull DatabaseMinecraftAccountDeleteAction delete() {
        return new DatabaseMinecraftAccountDeleteAction(this);
    }
}
