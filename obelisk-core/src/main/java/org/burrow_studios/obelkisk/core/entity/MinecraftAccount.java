package org.burrow_studios.obelkisk.core.entity;

import org.burrow_studios.obelkisk.core.db.interfaces.MinecraftAccountDB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class MinecraftAccount {
    private final UUID uuid;
    private final MinecraftAccountDB database;

    public MinecraftAccount(@NotNull UUID uuid, @NotNull MinecraftAccountDB database) {
        this.uuid = uuid;
        this.database = database;
    }

    public @NotNull UUID getUUID() {
        return this.uuid;
    }

    public @Nullable User getUser() {
        return this.database.getMinecraftAccountUser(this.uuid);
    }

    public @NotNull String getName() {
        return this.database.getMinecraftAccountName(this.uuid);
    }

    public void setUser(@Nullable User user) {
        this.database.setMinecraftAccountUser(this.uuid, user);
    }

    public void setName(@NotNull String name) {
        this.database.setMinecraftAccountName(this.uuid, name);
    }

    public void delete() {
        this.database.deleteMinecraftAccount(this.uuid);
    }
}
