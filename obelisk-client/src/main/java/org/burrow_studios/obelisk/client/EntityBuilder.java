package org.burrow_studios.obelisk.client;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.client.entities.*;
import org.jetbrains.annotations.NotNull;

public class EntityBuilder {
    private final ObeliskImpl obelisk;

    public EntityBuilder(@NotNull ObeliskImpl obelisk) {
        this.obelisk = obelisk;
    }

    public @NotNull UserImpl buildUser(@NotNull JsonObject data) {
        // TODO
        return null;
    }

    public @NotNull TicketImpl buildTicket(@NotNull JsonObject data) {
        // TODO
        return null;
    }

    public @NotNull ProjectImpl buildProject(@NotNull JsonObject data) {
        // TODO
        return null;
    }

    public @NotNull DiscordAccountImpl buildDiscordAccount(@NotNull JsonObject data) {
        // TODO
        return null;
    }

    public @NotNull MinecraftAccountImpl buildMinecraftAccount(@NotNull JsonObject data) {
        // TODO
        return null;
    }
}
