package org.burrow_studios.obelisk.client.action.entity.user;

import org.burrow_studios.obelisk.api.action.entity.user.UserBuilder;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.client.EntityBuilder;
import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.client.action.BuilderImpl;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;

public class UserBuilderImpl extends BuilderImpl<User> implements UserBuilder {
    public UserBuilderImpl(@NotNull ObeliskImpl obelisk) {
        super(obelisk, createRoute(), EntityBuilder::buildUser);
    }

    private static Route.Compiled createRoute() {
        return Route.User.CREATE_USER.compile();
    }

    @Override
    public @NotNull UserBuilderImpl setName(@NotNull String name) {
        this.data.addProperty("name", name);
        return this;
    }
}
