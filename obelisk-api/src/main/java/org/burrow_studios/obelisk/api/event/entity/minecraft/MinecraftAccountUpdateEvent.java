package org.burrow_studios.obelisk.api.event.entity.minecraft;

import org.burrow_studios.obelisk.api.entities.MinecraftAccount;
import org.burrow_studios.obelisk.api.event.entity.EntityUpdateEvent;
import org.jetbrains.annotations.NotNull;

public abstract class MinecraftAccountUpdateEvent<T> extends MinecraftAccountEvent implements EntityUpdateEvent<MinecraftAccount, T> {
    protected final T oldValue;
    protected final T newValue;

    protected MinecraftAccountUpdateEvent(long id, @NotNull MinecraftAccount entity, T oldValue, T newValue) {
        super(id, entity);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public T getOldValue() {
        return oldValue;
    }

    @Override
    public T getNewValue() {
        return newValue;
    }
}
