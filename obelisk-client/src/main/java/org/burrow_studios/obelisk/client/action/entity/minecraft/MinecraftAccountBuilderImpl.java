package org.burrow_studios.obelisk.client.action.entity.minecraft;

import org.burrow_studios.obelisk.api.action.entity.discord.MinecraftAccountBuilder;
import org.burrow_studios.obelisk.api.entities.MinecraftAccount;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.client.EntityBuilder;
import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.client.action.BuilderImpl;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MinecraftAccountBuilderImpl extends BuilderImpl<MinecraftAccount> implements MinecraftAccountBuilder {
    public MinecraftAccountBuilderImpl(@NotNull ObeliskImpl obelisk) {
        super(obelisk, createRoute(), EntityBuilder::buildMinecraftAccount);
    }

    private static Route.Compiled createRoute() {
        return Route.Minecraft.CREATE_MINECRAFT_ACCOUNT.compile();
    }

    @Override
    public @NotNull MinecraftAccountBuilder setUUID(@NotNull UUID uuid) {
        this.data.addProperty("uuid", uuid.toString());
        return this;
    }

    @Override
    public @NotNull MinecraftAccountBuilderImpl setCachedName(@NotNull String name) {
        this.data.addProperty("name", name);
        return this;
    }

    @Override
    public @NotNull MinecraftAccountBuilderImpl setUser(@NotNull User user) {
        this.data.addProperty("user", user.getId());
        return this;
    }
}
