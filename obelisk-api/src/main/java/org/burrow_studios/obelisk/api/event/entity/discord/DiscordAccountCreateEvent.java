package org.burrow_studios.obelisk.api.event.entity.discord;

import org.burrow_studios.obelisk.api.entities.DiscordAccount;
import org.burrow_studios.obelisk.api.event.entity.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public class DiscordAccountCreateEvent extends DiscordAccountEvent implements EntityCreateEvent<DiscordAccount> {
    public DiscordAccountCreateEvent(long id, @NotNull DiscordAccount entity) {
        super(id, entity);
    }
}
