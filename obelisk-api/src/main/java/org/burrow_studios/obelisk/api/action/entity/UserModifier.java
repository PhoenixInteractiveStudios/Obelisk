package org.burrow_studios.obelisk.api.action.entity;

import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface UserModifier extends Modifier<User> {
    @NotNull UserModifier setName(@NotNull String name);

    @NotNull UserModifier addDiscordIds(long... ids);

    @NotNull UserModifier removeDiscordIds(long... ids);

    @NotNull UserModifier addMinecraftIds(@NotNull UUID... ids);

    @NotNull UserModifier removeMinecraftIds(@NotNull UUID... ids);
}
