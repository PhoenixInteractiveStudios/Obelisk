package org.burrow_studios.obelisk.monolith.entities;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.user.UserModifier;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.cache.OrderedEntitySetView;
import org.burrow_studios.obelisk.core.entities.AbstractDiscordAccount;
import org.burrow_studios.obelisk.core.entities.AbstractMinecraftAccount;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.jetbrains.annotations.NotNull;

public class BackendUser extends AbstractUser {
    public BackendUser(
            @NotNull ObeliskMonolith obelisk,
            long id,
            @NotNull String name,
            @NotNull OrderedEntitySetView<AbstractDiscordAccount> discordAccounts,
            @NotNull OrderedEntitySetView<AbstractMinecraftAccount> minecraftAccounts
    ) {
        super(obelisk, id, name, discordAccounts, minecraftAccounts);
    }

    @Override
    public @NotNull UserModifier modify() {
        // TODO
        return null;
    }

    @Override
    public @NotNull DeleteAction<User> delete() {
        // TODO
        return null;
    }
}
