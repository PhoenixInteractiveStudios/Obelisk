package org.burrow_studios.obelisk.client.entities;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.client.action.DeleteActionImpl;
import org.burrow_studios.obelisk.client.action.entity.user.UserModifierImpl;
import org.burrow_studios.obelisk.core.cache.OrderedEntitySetView;
import org.burrow_studios.obelisk.core.entities.AbstractDiscordAccount;
import org.burrow_studios.obelisk.core.entities.AbstractMinecraftAccount;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;

public class UserImpl extends AbstractUser {
    public UserImpl(
            @NotNull ObeliskImpl obelisk,
            long id,
            @NotNull String name,
            @NotNull OrderedEntitySetView<AbstractDiscordAccount> discordAccounts,
            @NotNull OrderedEntitySetView<AbstractMinecraftAccount> minecraftAccounts
    ) {
        super(obelisk, id, name, discordAccounts, minecraftAccounts);
    }

    @Override
    public @NotNull UserModifierImpl modify() {
        return new UserModifierImpl(this);
    }

    @Override
    public @NotNull DeleteAction<User> delete() {
        Route.Compiled route = Route.User.DELETE_USER.compile(this.id);

        return new DeleteActionImpl<>(((ObeliskImpl) this.getAPI()), route, this.getId(), User.class);
    }
}
