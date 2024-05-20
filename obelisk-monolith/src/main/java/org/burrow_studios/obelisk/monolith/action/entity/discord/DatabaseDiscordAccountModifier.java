package org.burrow_studios.obelisk.monolith.action.entity.discord;

import org.burrow_studios.obelisk.api.action.entity.minecraft.DiscordAccountModifier;
import org.burrow_studios.obelisk.api.entities.DiscordAccount;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.monolith.action.DatabaseModifier;
import org.burrow_studios.obelisk.monolith.entities.BackendDiscordAccount;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DatabaseDiscordAccountModifier extends DatabaseModifier<DiscordAccount> implements DiscordAccountModifier {
    private String name;
    private User user;

    public DatabaseDiscordAccountModifier(@NotNull BackendDiscordAccount entity) {
        super(entity);
    }

    @Override
    public @NotNull DatabaseDiscordAccountModifier setCachedName(@NotNull String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public @NotNull DatabaseDiscordAccountModifier setUser(@Nullable User user) {
        this.user = user;
        return this;
    }

    public User getUser() {
        return this.user;
    }
}