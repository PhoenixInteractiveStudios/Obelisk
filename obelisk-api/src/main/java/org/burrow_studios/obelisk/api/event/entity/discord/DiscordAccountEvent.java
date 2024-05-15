package org.burrow_studios.obelisk.api.event.entity.discord;

import org.burrow_studios.obelisk.api.entities.DiscordAccount;
import org.burrow_studios.obelisk.api.event.entity.AbstractEntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class DiscordAccountEvent extends AbstractEntityEvent<DiscordAccount> {
    protected DiscordAccountEvent(long id, @NotNull DiscordAccount entity) {
        super(id, entity);
    }
}
