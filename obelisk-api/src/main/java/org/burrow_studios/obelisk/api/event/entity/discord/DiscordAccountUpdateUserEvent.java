package org.burrow_studios.obelisk.api.event.entity.discord;

import org.burrow_studios.obelisk.api.entities.DiscordAccount;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiscordAccountUpdateUserEvent extends DiscordAccountUpdateEvent<User> {
    public DiscordAccountUpdateUserEvent(long id, @NotNull DiscordAccount entity, @Nullable User oldValue, @Nullable User newValue) {
        super(id, entity, oldValue, newValue);
    }
}
