package org.burrow_studios.obelisk.api.action.entity.user;

import org.burrow_studios.obelisk.api.action.Builder;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface UserBuilder extends Builder<User> {
    @NotNull UserBuilder setName(@NotNull String name);

    @NotNull UserBuilder addDiscordIds(long... ids);

    @NotNull UserBuilder addMinecraftIds(@NotNull UUID... ids);
}
