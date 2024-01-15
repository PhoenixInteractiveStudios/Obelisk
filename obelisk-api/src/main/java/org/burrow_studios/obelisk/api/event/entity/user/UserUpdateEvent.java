package org.burrow_studios.obelisk.api.event.entity.user;

import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.event.entity.EntityUpdateEvent;
import org.jetbrains.annotations.NotNull;

public abstract sealed class UserUpdateEvent<T> extends UserEvent implements EntityUpdateEvent<User, T> permits UserUpdateDiscordIdsEvent, UserUpdateMinecraftIdsEvent, UserUpdateNameEvent {
    protected final T oldValue;
    protected final T newValue;

    protected UserUpdateEvent(long id, @NotNull User entity, T oldValue, T newValue) {
        super(id, entity);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public final T getOldValue() {
        return this.oldValue;
    }

    @Override
    public final T getNewValue() {
        return this.newValue;
    }
}
