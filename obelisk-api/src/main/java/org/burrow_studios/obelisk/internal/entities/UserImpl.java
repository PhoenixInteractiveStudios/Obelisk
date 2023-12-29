package org.burrow_studios.obelisk.internal.entities;

import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public final class UserImpl extends TurtleImpl implements User {
    private @NotNull String name;
    private final @NotNull List<Long> discordIds;
    private final @NotNull List<UUID> minecraftIds;

    public UserImpl(
            @NotNull ObeliskImpl api,
            long id,
            @NotNull String name,
            @NotNull List<Long> discordIds,
            @NotNull List<UUID> minecraftIds
    ) {
        super(api, id);
        this.name = name;
        this.discordIds = discordIds;
        this.minecraftIds = minecraftIds;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public @NotNull List<Long> getDiscordIds() {
        return List.copyOf(this.discordIds);
    }

    public @NotNull List<Long> getDiscordIdsMutable() {
        return this.discordIds;
    }

    @Override
    public @NotNull List<UUID> getMinecraftIds() {
        return List.copyOf(this.minecraftIds);
    }

    public @NotNull List<UUID> getMinecraftIdsMutable() {
        return this.minecraftIds;
    }
}
