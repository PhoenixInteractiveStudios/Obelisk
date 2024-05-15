package org.burrow_studios.obelisk.api.action.entity.minecraft;

import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.DiscordAccount;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

public interface DiscordAccountModifier extends Modifier<DiscordAccount> {
    @NotNull DiscordAccountModifier setCachedName(@NotNull String name);

    @NotNull DiscordAccountModifier setUser(@NotNull User user);
}
