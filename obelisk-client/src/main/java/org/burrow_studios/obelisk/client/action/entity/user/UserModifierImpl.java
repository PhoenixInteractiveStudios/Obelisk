package org.burrow_studios.obelisk.client.action.entity.user;

import org.burrow_studios.obelisk.api.action.entity.user.UserModifier;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.client.action.ModifierImpl;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;

public class UserModifierImpl extends ModifierImpl<User> implements UserModifier {
    public UserModifierImpl(@NotNull User entity) {
        super(entity, createRoute(entity));
    }

    private static Route.Compiled createRoute(@NotNull User entity) {
        return Route.User.EDIT_USER.compile(entity.getId());
    }

    @Override
    public @NotNull UserModifierImpl setName(@NotNull String name) {
        this.data.addProperty("name", name);
        return this;
    }
}
