package org.burrow_studios.obelisk.api.entities;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public interface User extends Turtle {
    @NotNull String getName();

    @NotNull List<Long> getDiscordIds();

    @NotNull List<UUID> getMinecraftIds();
}
