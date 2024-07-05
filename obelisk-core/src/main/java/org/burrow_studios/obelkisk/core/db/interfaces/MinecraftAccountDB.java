package org.burrow_studios.obelkisk.core.db.interfaces;

import org.burrow_studios.obelisk.api.entity.User;
import org.burrow_studios.obelkisk.core.entity.DatabaseMinecraftAccount;
import org.burrow_studios.obelkisk.core.entity.DatabaseUser;
import org.burrow_studios.obelkisk.core.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface MinecraftAccountDB {
    @NotNull DatabaseMinecraftAccount createMinecraftAccount(@NotNull UUID uuid, @NotNull String name) throws DatabaseException;


    @NotNull List<DatabaseMinecraftAccount> listMinecraftAccounts() throws DatabaseException;

    @NotNull DatabaseMinecraftAccount getMinecraftAccount(@NotNull UUID uuid) throws DatabaseException;

    @Nullable DatabaseUser getMinecraftAccountUser(@NotNull UUID uuid) throws DatabaseException;

    @NotNull String getMinecraftAccountName(@NotNull UUID uuid) throws DatabaseException;


    void setMinecraftAccountUser(@NotNull UUID uuid, @Nullable User user) throws DatabaseException;

    void setMinecraftAccountName(@NotNull UUID uuid, @NotNull String name) throws DatabaseException;


    void deleteMinecraftAccount(@NotNull UUID uuid) throws DatabaseException;
}
