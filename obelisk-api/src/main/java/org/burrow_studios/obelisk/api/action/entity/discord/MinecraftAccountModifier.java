package org.burrow_studios.obelisk.api.action.entity.discord;

import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.MinecraftAccount;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MinecraftAccountModifier extends Modifier<MinecraftAccount> {
    @NotNull MinecraftAccountModifier setCachedName(@NotNull String name);

    @NotNull MinecraftAccountModifier setUser(@Nullable User user);
}
