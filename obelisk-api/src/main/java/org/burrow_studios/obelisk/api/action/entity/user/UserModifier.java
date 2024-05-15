package org.burrow_studios.obelisk.api.action.entity.user;

import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

public interface UserModifier extends Modifier<User> {
    @NotNull UserModifier setName(@NotNull String name);
}
