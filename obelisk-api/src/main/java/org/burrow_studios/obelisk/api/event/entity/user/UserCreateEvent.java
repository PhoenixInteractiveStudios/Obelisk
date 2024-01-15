package org.burrow_studios.obelisk.api.event.entity.user;

import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.event.entity.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public final class UserCreateEvent extends UserEvent implements EntityCreateEvent<User> {
    public UserCreateEvent(long id, @NotNull User entity) {
        super(id, entity);
    }

    @Override
    public int getOpcode() {
        return 130;
    }
}
