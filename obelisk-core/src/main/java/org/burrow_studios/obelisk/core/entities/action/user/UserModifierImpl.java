package org.burrow_studios.obelisk.core.entities.action.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.user.UserModifier;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.action.ModifierImpl;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static org.burrow_studios.obelisk.core.entities.UpdateHelper.handleUpdate;
import static org.burrow_studios.obelisk.core.entities.UpdateHelper.handleUpdateArray;

public class UserModifierImpl extends ModifierImpl<User, UserImpl> implements UserModifier {
    public UserModifierImpl(@NotNull UserImpl user) {
        super(
                user,
                Route.User.EDIT.builder()
                        .withArg(user.getId())
                        .compile(),
                UserModifierImpl::update
        );
    }

    protected static void update(@NotNull EntityData data, @NotNull UserImpl user) {
        final JsonObject json = data.toJson();

        handleUpdate(json, "name", JsonElement::getAsString, user::setName);
        handleUpdateArray(json, "discord", JsonElement::getAsLong, user.getDiscordIdsMutable());
        handleUpdateArray(json, "minecraft", j -> UUID.fromString(j.getAsString()), user.getMinecraftIdsMutable());
    }

    @Override
    public @NotNull UserModifierImpl setName(@NotNull String name) {
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
