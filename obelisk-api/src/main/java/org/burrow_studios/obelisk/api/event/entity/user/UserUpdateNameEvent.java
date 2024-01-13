package org.burrow_studios.obelisk.api.event.entity.user;

import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

public final class UserUpdateNameEvent extends UserUpdateEvent<String> {
    public UserUpdateNameEvent(long id, @NotNull User entity, @NotNull String oldValue, @NotNull String newValue) {
        super(id, entity, oldValue, newValue);
    }
}
