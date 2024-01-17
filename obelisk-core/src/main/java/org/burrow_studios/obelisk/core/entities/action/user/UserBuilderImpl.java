package org.burrow_studios.obelisk.core.entities.action.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.user.UserBuilder;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.BuilderImpl;
import org.burrow_studios.obelisk.core.entities.checks.UserChecks;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UserBuilderImpl extends BuilderImpl<User> implements UserBuilder {
    public UserBuilderImpl(@NotNull ObeliskImpl api) {
        super(
                api,
                User.class,
                Route.User.CREATE.builder().compile(),
                UserImpl::new
        );
    }

    @Override
    public @NotNull UserBuilderImpl setName(@NotNull String name) throws IllegalArgumentException {
        UserChecks.checkName(name);
        data.set("name", new JsonPrimitive(name));
        return this;
    }

    @Override
    public @NotNull UserBuilderImpl addDiscordIds(long... ids) {
        JsonArray arr = new JsonArray();
        for (long id : ids)
            arr.add(id);
        data.addToArray("discord", arr);
        return this;
    }

    @Override
    public @NotNull UserBuilderImpl addMinecraftIds(@NotNull UUID... ids) {
        JsonArray arr = new JsonArray();
        for (UUID id : ids)
            arr.add(id.toString());
        data.addToArray("minecraft", arr);
        return this;
    }
}
