package org.burrow_studios.obelisk.client;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.core.entities.*;
import org.jetbrains.annotations.NotNull;

public class EntityBuilder {
    private final ObeliskImpl obelisk;

    public EntityBuilder(@NotNull ObeliskImpl obelisk) {
        this.obelisk = obelisk;
    }

    public @NotNull AbstractUser buildUser(@NotNull JsonObject data) {
        // TODO
        return null;
    }

    public @NotNull AbstractTicket buildTicket(@NotNull JsonObject data) {
        // TODO
        return null;
    }

    public @NotNull AbstractProject buildProject(@NotNull JsonObject data) {
        // TODO
        return null;
    }

    public @NotNull AbstractDiscordAccount buildDiscordAccount(@NotNull JsonObject data) {
        // TODO
        return null;
    }

    public @NotNull AbstractMinecraftAccount buildMinecraftAccount(@NotNull JsonObject data) {
        // TODO
        return null;
    }
}
