package org.burrow_studios.obelisk.api.event.entity.user;

import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.event.entity.AbstractEntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class UserEvent extends AbstractEntityEvent<User> {
    protected UserEvent(long id, @NotNull User entity) {
        super(id, entity);
    }
}
