package org.burrow_studios.obelisk.api.entity.dao;

import org.burrow_studios.obelisk.api.entity.MinecraftAccount;
import org.burrow_studios.obelisk.api.entity.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface MinecraftAccountDAO {
    @NotNull MinecraftAccount createMinecraftAccount(@NotNull UUID uuid, @NotNull String name);

    @NotNull List<? extends MinecraftAccount> listMinecraftAccounts();
    @NotNull MinecraftAccount getMinecraftAccount(@NotNull UUID uuid);
    @Nullable User getMinecraftAccountUser(@NotNull UUID uuid);
    @NotNull String getMinecraftAccountName(@NotNull UUID uuid);

    void setMinecraftAccountUser(@NotNull UUID uuid, @Nullable User user);
    void setMinecraftAccountName(@NotNull UUID uuid, @NotNull String name);

    void deleteMinecraftAccount(@NotNull UUID uuid);
}
