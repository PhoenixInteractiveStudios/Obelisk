package org.burrow_studios.obelisk.api.entities;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.discord.MinecraftAccountModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface MinecraftAccount extends IEntity {
    String IDENTIFIER = "minecraft";

    @Override
    @NotNull MinecraftAccountModifier modify();

    @Override
    @NotNull DeleteAction<MinecraftAccount> delete();

    /* - - - - - - - - - - */

    @NotNull UUID getUUID();

    @NotNull String getCachedName();

    @Nullable User getUser();
}
