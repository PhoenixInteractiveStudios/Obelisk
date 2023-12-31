package org.burrow_studios.obelisk.internal.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class UserData extends Data<User> {
    public UserData() {
        super();
    }

    public UserData(long id) {
        super(id);
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
