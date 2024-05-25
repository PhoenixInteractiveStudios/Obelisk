package org.burrow_studios.obelisk.api.entities;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.discord.MinecraftAccountBuilder;
import org.burrow_studios.obelisk.api.action.entity.discord.MinecraftAccountModifier;
import org.burrow_studios.obelisk.api.action.entity.minecraft.DiscordAccountBuilder;
import org.burrow_studios.obelisk.api.action.entity.minecraft.DiscordAccountModifier;
import org.burrow_studios.obelisk.api.action.entity.user.UserModifier;
import org.burrow_studios.obelisk.api.cache.OrderedEntitySet;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface User extends IEntity {
    String IDENTIFIER = "user";

    @Override
    @NotNull UserModifier modify();

    @Override
    @NotNull DeleteAction<User> delete();

    /* - - - - - - - - - - */

    @NotNull String getName();

    @NotNull OrderedEntitySet<? extends DiscordAccount> getDiscordAccounts();

    @NotNull OrderedEntitySet<? extends MinecraftAccount> getMinecraftAccounts();

    default @NotNull DiscordAccountModifier addDiscordAccount(@NotNull DiscordAccount discordAccount) {
        return discordAccount.modify()
                .setUser(this);
    }

    default @NotNull MinecraftAccountModifier addMinecraftAccount(@NotNull MinecraftAccount minecraftAccount) {
        return minecraftAccount.modify()
                .setUser(this);
    }

    default @NotNull DiscordAccountBuilder createDiscordAccount(long snowflake, @NotNull String name) {
        return this.getAPI().createDiscordAccount()
                .setSnowflake(snowflake)
                .setCachedName(name)
                .setUser(this);
    }

    default @NotNull MinecraftAccountBuilder createMinecraftAccount(@NotNull UUID uuid, @NotNull String name) {
        return this.getAPI().createMinecraftAccount()
                .setUUID(uuid)
                .setCachedName(name)
                .setUser(this);
    }
}
