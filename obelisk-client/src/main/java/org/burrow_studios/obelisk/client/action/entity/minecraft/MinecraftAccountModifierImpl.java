package org.burrow_studios.obelisk.client.action.entity.minecraft;

import com.google.gson.JsonNull;
import org.burrow_studios.obelisk.api.action.entity.discord.MinecraftAccountModifier;
import org.burrow_studios.obelisk.api.entities.MinecraftAccount;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.client.action.ModifierImpl;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MinecraftAccountModifierImpl extends ModifierImpl<MinecraftAccount> implements MinecraftAccountModifier {
    public MinecraftAccountModifierImpl(@NotNull MinecraftAccount entity) {
        super(entity, createRoute(entity));
    }

    private static Route.Compiled createRoute(@NotNull MinecraftAccount entity) {
        return Route.Minecraft.EDIT_MINECRAFT_ACCOUNT.compile(entity.getId());
    }

    @Override
    public @NotNull MinecraftAccountModifierImpl setCachedName(@NotNull String name) {
        this.data.addProperty("name", name);
        return this;
    }

    @Override
    public @NotNull MinecraftAccountModifierImpl setUser(@Nullable User user) {
        if (user == null)
            this.data.add("user", JsonNull.INSTANCE);
        else
            this.data.addProperty("user", user.getId());
        return this;
    }
}
