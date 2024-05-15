package org.burrow_studios.obelisk.api.action.entity.discord;

import org.burrow_studios.obelisk.api.action.Builder;
import org.burrow_studios.obelisk.api.entities.MinecraftAccount;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface MinecraftAccountBuilder extends Builder<MinecraftAccount> {
    @NotNull MinecraftAccountBuilder setUUID(@NotNull UUID uuid);

    @NotNull MinecraftAccountBuilder setCachedName(@NotNull String name);

    @NotNull MinecraftAccountBuilder setUser(@NotNull User user);
}
