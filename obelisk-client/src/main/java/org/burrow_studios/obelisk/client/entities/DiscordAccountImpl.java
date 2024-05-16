package org.burrow_studios.obelisk.client.entities;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.entities.DiscordAccount;
import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.client.action.DeleteActionImpl;
import org.burrow_studios.obelisk.client.action.entity.discord.DiscordAccountModifierImpl;
import org.burrow_studios.obelisk.core.entities.AbstractDiscordAccount;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;

public class DiscordAccountImpl extends AbstractDiscordAccount {
    public DiscordAccountImpl(
            @NotNull ObeliskImpl obelisk,
            long id,
            long snowflake,
            @NotNull String cachedName,
            @NotNull AbstractUser user
    ) {
        super(obelisk, id, snowflake, cachedName, user);
    }

    @Override
    public @NotNull DiscordAccountModifierImpl modify() {
        return new DiscordAccountModifierImpl(this);
    }

    @Override
    public @NotNull DeleteAction<DiscordAccount> delete() {
        Route.Compiled route = Route.Discord.DELETE_DISCORD_ACCOUNT.compile(this.id);

        return new DeleteActionImpl<>(((ObeliskImpl) this.getAPI()), route, this.getId(), DiscordAccount.class);
    }
}
