package org.burrow_studios.obelisk.api.event.entity.minecraft;

import org.burrow_studios.obelisk.api.entities.MinecraftAccount;
import org.jetbrains.annotations.NotNull;

public class MinecraftAccountUpdateNameEvent extends MinecraftAccountUpdateEvent<String> {
    public MinecraftAccountUpdateNameEvent(long id, @NotNull MinecraftAccount entity, @NotNull String oldValue, @NotNull String newValue) {
        super(id, entity, oldValue, newValue);
    }
}
