package org.burrow_studios.obelisk.api.event.entity.discord;

import org.burrow_studios.obelisk.api.entities.DiscordAccount;
import org.jetbrains.annotations.NotNull;

public class DiscordAccountUpdateNameEvent extends DiscordAccountUpdateEvent<String> {
    public DiscordAccountUpdateNameEvent(long id, @NotNull DiscordAccount entity, @NotNull String oldValue, @NotNull String newValue) {
        super(id, entity, oldValue, newValue);
    }
}
