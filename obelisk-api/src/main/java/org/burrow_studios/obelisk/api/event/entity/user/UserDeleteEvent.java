package org.burrow_studios.obelisk.api.event.entity.user;

import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public class UserDeleteEvent extends UserEvent implements EntityDeleteEvent<User> {
    public UserDeleteEvent(long id, @NotNull User entity) {
        super(id, entity);
    }
}
