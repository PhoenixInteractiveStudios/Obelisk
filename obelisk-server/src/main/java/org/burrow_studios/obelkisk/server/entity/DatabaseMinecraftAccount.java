package org.burrow_studios.obelkisk.server.entity;

import org.burrow_studios.obelisk.api.entity.MinecraftAccount;
import org.burrow_studios.obelisk.api.entity.User;
import org.burrow_studios.obelkisk.server.db.interfaces.MinecraftAccountDB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class DatabaseMinecraftAccount implements MinecraftAccount {
    private final UUID uuid;
    private final MinecraftAccountDB database;

    public DatabaseMinecraftAccount(@NotNull UUID uuid, @NotNull MinecraftAccountDB database) {
        this.uuid = uuid;
        this.database = database;
    }

    @Override
    public @NotNull UUID getUUID() {
        return this.uuid;
    }

    @Override
    public @Nullable DatabaseUser getUser() {
        return this.database.getMinecraftAccountUser(this.uuid);
    }

    @Override
    public @NotNull String getName() {
        return this.database.getMinecraftAccountName(this.uuid);
    }

    @Override
    public void setUser(@Nullable User user) {
        this.database.setMinecraftAccountUser(this.uuid, user);
    }

    @Override
    public void setName(@NotNull String name) {
        this.database.setMinecraftAccountName(this.uuid, name);
    }

    @Override
    public void delete() {
        this.database.deleteMinecraftAccount(this.uuid);
    }
}
