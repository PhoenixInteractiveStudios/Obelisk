package org.burrow_studios.obelisk.api.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DiscordAccount {
    long getSnowflake();
    @Nullable User getUser();
    @NotNull String getName();

    void setUser(@Nullable User user);
    void setName(@NotNull String name);

    void delete();
}
