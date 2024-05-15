package org.burrow_studios.obelisk.api.entities;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DiscordAccount extends IEntity {
    @Override
    @NotNull DeleteAction<DiscordAccount> delete();

    /* - - - - - - - - - - */

    long getSnowflake();

    @NotNull String getCachedName();

    @Nullable User getUser();
}
