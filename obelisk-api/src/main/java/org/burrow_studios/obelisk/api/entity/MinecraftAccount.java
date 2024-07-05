package org.burrow_studios.obelisk.api.entity;

import org.burrow_studios.obelisk.api.entity.dao.MinecraftAccountDAO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class MinecraftAccount {
    private final UUID uuid;
    private final MinecraftAccountDAO dao;

    public MinecraftAccount(@NotNull UUID uuid, @NotNull MinecraftAccountDAO dao) {
        this.uuid = uuid;
        this.dao = dao;
    }

    public @NotNull UUID getUUID() {
        return this.uuid;
    }

    public @Nullable User getUser() {
        return this.dao.getMinecraftAccountUser(this.uuid);
    }

    public @NotNull String getName() {
        return this.dao.getMinecraftAccountName(this.uuid);
    }

    public void setUser(@Nullable User user) {
        this.dao.setMinecraftAccountUser(this.uuid, user);
    }

    public void setName(@NotNull String name) {
        this.dao.setMinecraftAccountName(this.uuid, name);
    }

    public void delete() {
        this.dao.deleteMinecraftAccount(this.uuid);
    }
}
