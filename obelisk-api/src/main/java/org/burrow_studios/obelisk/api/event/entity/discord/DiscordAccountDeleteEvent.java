package org.burrow_studios.obelisk.api.event.entity.discord;

import org.burrow_studios.obelisk.api.entities.DiscordAccount;
import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public class DiscordAccountDeleteEvent extends DiscordAccountEvent implements EntityDeleteEvent<DiscordAccount> {
    public DiscordAccountDeleteEvent(long id, @NotNull DiscordAccount entity) {
        super(id, entity);
    }
}
