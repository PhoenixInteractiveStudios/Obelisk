package org.burrow_studios.obelisk.core.entities.action.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.user.UserModifier;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.action.ModifierImpl;
import org.burrow_studios.obelisk.core.entities.EntityUpdater;
import org.burrow_studios.obelisk.core.entities.checks.UserChecks;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.burrow_studios.obelisk.commons.rpc.Endpoints;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UserModifierImpl extends ModifierImpl<User, UserImpl> implements UserModifier {
    public UserModifierImpl(@NotNull UserImpl user) {
        super(
                user,
                Endpoints.User.EDIT.builder(user.getId()).getPath(),
                EntityUpdater::updateUser
        );
    }

    @Override
    public @NotNull UserModifierImpl setName(@NotNull String name) throws IllegalArgumentException {
        UserChecks.checkName(name);
        data.set("name", new JsonPrimitive(name));
        return this;
    }

    @Override
    public @NotNull UserModifierImpl addDiscordIds(long... ids) {
        JsonArray arr = new JsonArray();
        for (long id : ids)
            arr.add(id);
        data.addToArray("discord", arr);
        return this;
    }

    @Override
    public @NotNull UserModifierImpl removeDiscordIds(long... ids) {
        JsonArray arr = new JsonArray();
        for (long id : ids)
            arr.add(id);
        data.removeFromArray("discord", arr);
        return this;
    }

    @Override
    public @NotNull UserModifierImpl addMinecraftIds(@NotNull UUID... ids) {
        JsonArray arr = new JsonArray();
        for (UUID id : ids)
            arr.add(id.toString());
        data.addToArray("minecraft", arr);
        return this;
    }

    @Override
    public @NotNull UserModifierImpl removeMinecraftIds(@NotNull UUID... ids) {
        JsonArray arr = new JsonArray();
        for (UUID id : ids)
            arr.add(id.toString());
        data.removeFromArray("minecraft", arr);
        return this;
    }
}
