package org.burrow_studios.obelisk.api.event.entity.minecraft;

import org.burrow_studios.obelisk.api.entities.MinecraftAccount;
import org.burrow_studios.obelisk.api.event.entity.AbstractEntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class MinecraftAccountEvent extends AbstractEntityEvent<MinecraftAccount> {
    protected MinecraftAccountEvent(long id, @NotNull MinecraftAccount entity) {
        super(id, entity);
    }
}
