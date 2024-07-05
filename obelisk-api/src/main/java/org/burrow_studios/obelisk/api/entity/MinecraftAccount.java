package org.burrow_studios.obelisk.api.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface MinecraftAccount {
    @NotNull UUID getUUID();
    @Nullable User getUser();
    @NotNull String getName();

    void setUser(@Nullable User user);
    void setName(@NotNull String name);

    void delete();
}
