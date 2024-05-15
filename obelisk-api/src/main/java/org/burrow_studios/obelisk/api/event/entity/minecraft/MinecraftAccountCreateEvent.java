package org.burrow_studios.obelisk.api.event.entity.minecraft;

import org.burrow_studios.obelisk.api.entities.MinecraftAccount;
import org.burrow_studios.obelisk.api.event.entity.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public class MinecraftAccountCreateEvent extends MinecraftAccountEvent implements EntityCreateEvent<MinecraftAccount> {
    public MinecraftAccountCreateEvent(long id, @NotNull MinecraftAccount entity) {
        super(id, entity);
    }
}
