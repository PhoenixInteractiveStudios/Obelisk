package org.burrow_studios.obelisk.api.event.entity.minecraft;

import org.burrow_studios.obelisk.api.entities.MinecraftAccount;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MinecraftAccountUpdateUserEvent extends MinecraftAccountUpdateEvent<User> {
    public MinecraftAccountUpdateUserEvent(long id, @NotNull MinecraftAccount entity, @Nullable User oldValue, @Nullable User newValue) {
        super(id, entity, oldValue, newValue);
    }
}
