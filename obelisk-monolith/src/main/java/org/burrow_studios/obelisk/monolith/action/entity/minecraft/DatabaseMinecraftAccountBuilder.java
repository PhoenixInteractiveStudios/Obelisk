package org.burrow_studios.obelisk.monolith.action.entity.minecraft;

import org.burrow_studios.obelisk.api.action.entity.discord.MinecraftAccountBuilder;
import org.burrow_studios.obelisk.api.entities.MinecraftAccount;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DatabaseMinecraftAccountBuilder extends DatabaseBuilder<MinecraftAccount> implements MinecraftAccountBuilder {
    private UUID uuid;
    private String name;
    private User user;

    public DatabaseMinecraftAccountBuilder(@NotNull ObeliskMonolith obelisk) {
        super(obelisk);
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
