package org.burrow_studios.obelisk.api.event.entity.user;

import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class UserEvent extends EntityEvent<User> {
    protected UserEvent(@NotNull User entity) {
        super(entity);
    }
}
