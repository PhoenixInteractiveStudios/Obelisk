package org.burrow_studios.obelisk.client.action.entity.discord;

import org.burrow_studios.obelisk.api.action.entity.minecraft.DiscordAccountModifier;
import org.burrow_studios.obelisk.api.entities.DiscordAccount;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.client.action.ModifierImpl;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;

public class DiscordAccountModifierImpl extends ModifierImpl<DiscordAccount> implements DiscordAccountModifier {
    public DiscordAccountModifierImpl(@NotNull DiscordAccount entity) {
        super(entity, createRoute(entity));
    }

    private static Route.Compiled createRoute(@NotNull DiscordAccount entity) {
        return Route.Discord.EDIT_DISCORD_ACCOUNT.compile(entity.getId());
    }

    @Override
    public @NotNull DiscordAccountModifierImpl setCachedName(@NotNull String name) {
        this.data.addProperty("name", name);
        return this;
    }

    @Override
    public @NotNull DiscordAccountModifierImpl setUser(@NotNull User user) {
        this.data.addProperty("user", user.getId());
        return this;
    }
}
