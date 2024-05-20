package org.burrow_studios.obelisk.monolith.action.entity.minecraft;

import org.burrow_studios.obelisk.api.action.entity.discord.MinecraftAccountModifier;
import org.burrow_studios.obelisk.api.entities.MinecraftAccount;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.monolith.action.DatabaseModifier;
import org.burrow_studios.obelisk.monolith.entities.BackendMinecraftAccount;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DatabaseMinecraftAccountModifier extends DatabaseModifier<MinecraftAccount> implements MinecraftAccountModifier {
    private String name;
    private User user;

    public DatabaseMinecraftAccountModifier(@NotNull BackendMinecraftAccount entity) {
        super(entity);
    }

    @Override
    public @NotNull DatabaseMinecraftAccountModifier setCachedName(@NotNull String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public @NotNull DatabaseMinecraftAccountModifier setUser(@Nullable User user) {
        this.user = user;
        return this;
    }

    public User getUser() {
        return this.user;
    }
}