package org.burrow_studios.obelisk.api.event.entity.minecraft;

import org.burrow_studios.obelisk.api.entities.MinecraftAccount;
import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public class MinecraftAccountDeleteEvent extends MinecraftAccountEvent implements EntityDeleteEvent<MinecraftAccount> {
    public MinecraftAccountDeleteEvent(long id, @NotNull MinecraftAccount entity) {
        super(id, entity);
    }
}
