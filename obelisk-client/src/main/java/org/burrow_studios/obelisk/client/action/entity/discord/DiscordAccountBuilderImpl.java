package org.burrow_studios.obelisk.client.action.entity.discord;

import com.google.gson.JsonNull;
import org.burrow_studios.obelisk.api.action.entity.minecraft.DiscordAccountBuilder;
import org.burrow_studios.obelisk.api.entities.DiscordAccount;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.client.EntityBuilder;
import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.client.action.BuilderImpl;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiscordAccountBuilderImpl extends BuilderImpl<DiscordAccount> implements DiscordAccountBuilder {
    public DiscordAccountBuilderImpl(@NotNull ObeliskImpl obelisk) {
        super(obelisk, createRoute(), EntityBuilder::buildDiscordAccount);
    }

    private static Route.Compiled createRoute() {
        return Route.Discord.CREATE_DISCORD_ACCOUNT.compile();
    }

    @Override
    public @NotNull DiscordAccountBuilderImpl setSnowflake(long snowflake) {
        this.data.addProperty("snowflake", snowflake);
        return this;
    }

    @Override
    public @NotNull DiscordAccountBuilderImpl setCachedName(@NotNull String name) {
        this.data.addProperty("name", name);
        return this;
    }

    @Override
    public @NotNull DiscordAccountBuilderImpl setUser(@Nullable User user) {
        if (user == null)
            this.data.add("user", JsonNull.INSTANCE);
        else
            this.data.addProperty("user", user.getId());
        return this;
    }
}
