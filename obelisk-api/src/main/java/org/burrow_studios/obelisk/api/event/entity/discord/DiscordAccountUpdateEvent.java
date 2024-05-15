package org.burrow_studios.obelisk.api.event.entity.discord;

import org.burrow_studios.obelisk.api.entities.DiscordAccount;
import org.burrow_studios.obelisk.api.event.entity.EntityUpdateEvent;
import org.jetbrains.annotations.NotNull;

public abstract class DiscordAccountUpdateEvent<T> extends DiscordAccountEvent implements EntityUpdateEvent<DiscordAccount, T> {
    protected final T oldValue;
    protected final T newValue;

    protected DiscordAccountUpdateEvent(long id, @NotNull DiscordAccount entity, T oldValue, T newValue) {
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
