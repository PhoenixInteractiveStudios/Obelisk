package org.burrow_studios.obelisk.api.entities;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.cache.OrderedEntitySet;
import org.jetbrains.annotations.NotNull;

public interface User extends IEntity {
    @Override
    @NotNull DeleteAction<User> delete();

    /* - - - - - - - - - - */

    @NotNull String getName();

    @NotNull OrderedEntitySet<? extends DiscordAccount> getDiscordAccounts();

    @NotNull OrderedEntitySet<? extends MinecraftAccount> getMinecraftAccounts();
}
