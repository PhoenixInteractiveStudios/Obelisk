package org.burrow_studios.obelisk.server.users.db.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.JsonFileDatabase;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.Set;
import java.util.UUID;

class FileUserDB implements UserDB {
    private final JsonFileDatabase database;

    FileUserDB(@NotNull File dir) throws NotDirectoryException {
        this.database = new JsonFileDatabase(dir);
    }

    @Override
    public @NotNull Set<Long> getUserIds() throws DatabaseException {
        return database.getIds();
    }

    @Override
    public @NotNull JsonObject getUser(long id) throws DatabaseException {
        return database.get(id);
    }

    @Override
    public void createUser(long id, @NotNull String name) throws DatabaseException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("name", name);
        json.add("discord", new JsonArray());
        json.add("minecraft", new JsonArray());

        database.create(id, json);
    }

    @Override
    public void updateUserName(long id, @NotNull String name) throws DatabaseException {
        database.patch(id, json -> json.addProperty("name", name));
    }

    @Override
    public void addUserDiscordId(long user, long snowflake) throws DatabaseException {
        database.patch(user, json -> {
            JsonArray discord = json.getAsJsonArray("discord");
            discord.add(snowflake);
        });
    }

    @Override
    public void removeUserDiscordId(long user, long snowflake) throws DatabaseException {
        database.patch(user, json -> {
            JsonArray discord = json.getAsJsonArray("discord");
            discord.remove(new JsonPrimitive(snowflake));
        });
    }

    @Override
    public void addUserMinecraftId(long user, @NotNull UUID uuid) throws DatabaseException {
        database.patch(user, json -> {
            JsonArray minecraft = json.getAsJsonArray("minecraft");
            minecraft.add(uuid.toString());
        });
    }

    @Override
    public void removeUserMinecraftId(long user, @NotNull UUID uuid) throws DatabaseException {
        database.patch(user, json -> {
            JsonArray minecraft = json.getAsJsonArray("minecraft");
            minecraft.remove(new JsonPrimitive(uuid.toString()));
        });
    }

    @Override
    public void deleteUser(long id) throws DatabaseException {
        database.delete(id);
    }
}
