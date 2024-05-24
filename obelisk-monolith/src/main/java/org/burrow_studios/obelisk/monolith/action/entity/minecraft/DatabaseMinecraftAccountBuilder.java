package org.burrow_studios.obelisk.monolith.action.entity.minecraft;

import org.burrow_studios.obelisk.api.action.entity.discord.MinecraftAccountBuilder;
import org.burrow_studios.obelisk.api.entities.MinecraftAccount;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseBuilder;
import org.burrow_studios.obelisk.monolith.db.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendMinecraftAccount;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DatabaseMinecraftAccountBuilder extends DatabaseBuilder<MinecraftAccount> implements MinecraftAccountBuilder {
    private UUID uuid;
    private String name;
    private User user;

    public DatabaseMinecraftAccountBuilder(@NotNull ObeliskMonolith obelisk) {
        super(obelisk);
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<MinecraftAccount> future) throws DatabaseException {
        BackendMinecraftAccount minecraftAccount = actionableDatabase.createMinecraftAccount(this);
        future.complete(minecraftAccount);
    }

    @Override
    public @NotNull DatabaseMinecraftAccountBuilder setUUID(@NotNull UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public @NotNull DatabaseMinecraftAccountBuilder setCachedName(@NotNull String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public @NotNull DatabaseMinecraftAccountBuilder setUser(@Nullable User user) {
        this.user = user;
        return this;
    }

    public User getUser() {
        return this.user;
    }
}
