package org.burrow_studios.obelisk.core.entities.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public final class UserData extends Data<UserImpl> {
    public UserData() {
        super();
    }

    public UserData(long id) {
        super(id);
    }

    public UserData(@NotNull JsonObject json) {
        super(json);
    }

    @Override
    public @NotNull UserImpl build(@NotNull ObeliskImpl api) {
        final JsonObject json = toJson();

        final long   id   = json.get("id").getAsLong();
        final String name = json.get("name").getAsString();

        final ArrayList<Long>   discordIds = buildList(json, "discord", JsonElement::getAsLong);
        final ArrayList<UUID> minecraftIds = buildList(json, "minecraft", e -> UUID.fromString(e.getAsString()));

        final UserImpl user = new UserImpl(api, id, name, discordIds, minecraftIds);

        api.getUsers().add(user);
        return user;
    }

    @Override
    public void update(@NotNull UserImpl user) {
        final JsonObject json = toJson();

        handleUpdate(json, "name", JsonElement::getAsString, user::setName);
        handleUpdateArray(json, "discord", JsonElement::getAsLong, user.getDiscordIdsMutable());
        handleUpdateArray(json, "minecraft", j -> UUID.fromString(j.getAsString()), user.getMinecraftIdsMutable());
    }

    public void setName(@NotNull String name) {
        this.set("name", new JsonPrimitive(name));
    }

    public void addDiscordIds(long... ids) {
        JsonArray arr = new JsonArray();
        for (long id : ids)
            arr.add(id);
        this.addToArray("discord", arr);
    }

    public void removeDiscordIds(long... ids) {
        JsonArray arr = new JsonArray();
        for (long id : ids)
            arr.add(id);
        this.removeFromArray("discord", arr);
    }

    public void addMinecraftIds(@NotNull UUID... ids) {
        JsonArray arr = new JsonArray();
        for (UUID id : ids)
            arr.add(id.toString());
        this.addToArray("minecraft", arr);
    }

    public void removeMinecraftIds(@NotNull UUID... ids) {
        JsonArray arr = new JsonArray();
        for (UUID id : ids)
            arr.add(id.toString());
        this.removeFromArray("minecraft", arr);
    }
}
