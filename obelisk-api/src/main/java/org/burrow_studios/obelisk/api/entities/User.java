package org.burrow_studios.obelisk.api.entities;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.user.UserModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public interface User extends Turtle {
    @Override
    @NotNull UserModifier modify();

    @Override
    @NotNull DeleteAction<User> delete();

    @NotNull String getName();

    @NotNull List<Long> getDiscordIds();

    @NotNull List<UUID> getMinecraftIds();
}
