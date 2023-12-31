package org.burrow_studios.obelisk.api.entities;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.user.UserModifier;
import org.burrow_studios.obelisk.internal.entities.UserImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public sealed interface User extends Turtle permits UserImpl {
    @Override
    @NotNull UserModifier modify();

    @Override
    @NotNull DeleteAction<User> delete();

    @NotNull String getName();

    @NotNull List<Long> getDiscordIds();

    @NotNull List<UUID> getMinecraftIds();
}
