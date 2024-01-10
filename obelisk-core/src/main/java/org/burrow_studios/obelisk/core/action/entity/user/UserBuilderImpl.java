package org.burrow_studios.obelisk.core.action.entity.user;

import org.burrow_studios.obelisk.api.action.entity.user.UserBuilder;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.BuilderImpl;
import org.burrow_studios.obelisk.core.data.UserData;
import org.burrow_studios.obelisk.core.entities.UserImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UserBuilderImpl extends BuilderImpl<User, UserImpl, UserData> implements UserBuilder {
    public UserBuilderImpl(@NotNull ObeliskImpl api) {
        super(
                api,
                User.class,
                Route.User.CREATE.builder().compile(),
                new UserData(),
                UserData::new
        );
    }

    @Override
    public @NotNull UserBuilderImpl setName(@NotNull String name) {
        this.data.setName(name);
        return this;
    }

    @Override
    public @NotNull UserBuilderImpl addDiscordIds(long... ids) {
        this.data.addDiscordIds(ids);
        return this;
    }

    @Override
    public @NotNull UserBuilderImpl addMinecraftIds(@NotNull UUID... ids) {
        this.data.addMinecraftIds(ids);
        return this;
    }
}
