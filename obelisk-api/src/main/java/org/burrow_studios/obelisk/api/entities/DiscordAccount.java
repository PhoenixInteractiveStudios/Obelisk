package org.burrow_studios.obelisk.api.entities;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.minecraft.DiscordAccountModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DiscordAccount extends IEntity {
    String IDENTIFIER = "discord";

    @Override
    @NotNull DiscordAccountModifier modify();

    @Override
    @NotNull DeleteAction<DiscordAccount> delete();

    /* - - - - - - - - - - */

    long getSnowflake();

    @NotNull String getCachedName();

    @Nullable User getUser();
}
