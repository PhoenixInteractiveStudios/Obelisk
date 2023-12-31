package org.burrow_studios.obelisk.internal.action.entity.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.user.UserModifier;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.internal.action.ModifierImpl;
import org.burrow_studios.obelisk.internal.entities.UserImpl;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UserModifierImpl extends ModifierImpl<User> implements UserModifier {
    public UserModifierImpl(@NotNull UserImpl entity) {
        super(entity);
    }

    @Override
    public @NotNull UserModifierImpl setName(@NotNull String name) {
        this.set("name", new JsonPrimitive(name));
        return this;
    }

    @Override
    public @NotNull UserModifierImpl addDiscordIds(long... ids) {
        JsonArray arr = new JsonArray();
        for (long id : ids)
            arr.add(id);
        this.add("discord", arr);
        return this;
    }

    @Override
    public @NotNull UserModifierImpl removeDiscordIds(long... ids) {
        JsonArray arr = new JsonArray();
        for (long id : ids)
            arr.add(id);
        this.remove("discord", arr);
        return this;
    }

    @Override
    public @NotNull UserModifierImpl addMinecraftIds(@NotNull UUID... ids) {
        JsonArray arr = new JsonArray();
        for (UUID id : ids)
            arr.add(id.toString());
        this.add("minecraft", arr);
        return this;
    }

    @Override
    public @NotNull UserModifierImpl removeMinecraftIds(@NotNull UUID... ids) {
        JsonArray arr = new JsonArray();
        for (UUID id : ids)
            arr.add(id.toString());
        this.remove("minecraft", arr);
        return this;
    }
}
