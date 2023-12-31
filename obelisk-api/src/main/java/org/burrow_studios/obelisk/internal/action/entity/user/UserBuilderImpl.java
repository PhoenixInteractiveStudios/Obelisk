package org.burrow_studios.obelisk.internal.action.entity.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.user.UserBuilder;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.internal.EntityBuilder;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.action.BuilderImpl;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UserBuilderImpl extends BuilderImpl<User> implements UserBuilder {
    public UserBuilderImpl(@NotNull ObeliskImpl api) {
        super(
                api,
                User.class,
                Route.User.CREATE.builder().compile(),
                EntityBuilder::buildUser
        );
    }

    @Override
    public @NotNull UserBuilderImpl setName(@NotNull String name) {
        this.set("name", new JsonPrimitive(name));
        return this;
    }

    @Override
    public @NotNull UserBuilderImpl addDiscordIds(long... ids) {
        JsonArray arr = new JsonArray();
        for (long id : ids)
            arr.add(id);
        this.add("discord", arr);
        return this;
    }

    @Override
    public @NotNull UserBuilderImpl addMinecraftIds(@NotNull UUID... ids) {
        JsonArray arr = new JsonArray();
        for (UUID id : ids)
            arr.add(id.toString());
        this.add("minecraft", arr);
        return this;
    }
}
