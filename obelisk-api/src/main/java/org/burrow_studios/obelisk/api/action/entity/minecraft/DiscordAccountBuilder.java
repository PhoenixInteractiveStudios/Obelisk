package org.burrow_studios.obelisk.api.action.entity.minecraft;

import org.burrow_studios.obelisk.api.action.Builder;
import org.burrow_studios.obelisk.api.entities.DiscordAccount;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DiscordAccountBuilder extends Builder<DiscordAccount> {
    @NotNull DiscordAccountBuilder setSnowflake(long snowflake);

    @NotNull DiscordAccountBuilder setCachedName(@NotNull String name);

    @NotNull DiscordAccountBuilder setUser(@Nullable User user);
}
