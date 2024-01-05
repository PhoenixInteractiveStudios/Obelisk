package org.burrow_studios.obelisk.server.users.db.user;

import com.google.gson.*;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class FileUserDB implements UserDB {
    private final File dir;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    FileUserDB(@NotNull File dir) {
        this.dir = dir;
        dir.mkdirs();
    }

    @Override
    public @NotNull Set<Long> getUserIds() throws DatabaseException {
        File[] files = dir.listFiles();

        Set<Long> ids = new HashSet<>();

        if (files == null)
            throw new DatabaseException();

        for (File file : files) {
            String filename = file.getName();
            if (!filename.endsWith(".json")) continue;

            try {
                long id = Long.parseLong(filename.substring(0, filename.length() - ".json".length()));
                ids.add(id);
            } catch (NumberFormatException ignored) { }
        }

        return Set.copyOf(ids);
    }

    @Override
    public @NotNull JsonObject getUser(long id) throws DatabaseException {
        File file = new File(dir, id + ".json");

        if (!file.exists())
            throw new NoSuchEntryException();

        try {
            String content = Files.readString(file.toPath());
            return gson.fromJson(content, JsonObject.class);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void createUser(long id, @NotNull String name) throws DatabaseException {
        File file = new File(dir, id + ".json");

        if (file.exists()) return;

        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("name", name);
        json.add("discord", new JsonArray());
        json.add("minecraft", new JsonArray());

        String content = gson.toJson(json);

        try {
            Files.writeString(file.toPath(), content, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateUserName(long id, @NotNull String name) throws DatabaseException {
        File file = new File(dir, id + ".json");

        if (!file.exists())
            throw new NoSuchEntryException();

        JsonObject json;

        try {
            String content = Files.readString(file.toPath());
            json = gson.fromJson(content, JsonObject.class);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }

        json.addProperty("name", name);

        String content = gson.toJson(json);

        try {
            Files.writeString(file.toPath(), content, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void addUserDiscordId(long user, long snowflake) throws DatabaseException {
        File file = new File(dir, user + ".json");

        if (!file.exists())
            throw new NoSuchEntryException();

        JsonObject json;

        try {
            String content = Files.readString(file.toPath());
            json = gson.fromJson(content, JsonObject.class);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }

        JsonArray discord = json.getAsJsonArray("discord");
        discord.add(snowflake);

        String content = gson.toJson(json);

        try {
            Files.writeString(file.toPath(), content, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void removeUserDiscordId(long user, long snowflake) throws DatabaseException {
        File file = new File(dir, user + ".json");

        if (!file.exists())
            throw new NoSuchEntryException();

        JsonObject json;

        try {
            String content = Files.readString(file.toPath());
            json = gson.fromJson(content, JsonObject.class);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }

        JsonArray discord = json.getAsJsonArray("discord");
        discord.remove(new JsonPrimitive(snowflake));

        String content = gson.toJson(json);

        try {
            Files.writeString(file.toPath(), content, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void addUserMinecraftId(long user, @NotNull UUID uuid) throws DatabaseException {
        File file = new File(dir, user + ".json");

        if (!file.exists())
            throw new NoSuchEntryException();

        JsonObject json;

        try {
            String content = Files.readString(file.toPath());
            json = gson.fromJson(content, JsonObject.class);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }

        JsonArray minecraft = json.getAsJsonArray("minecraft");
        minecraft.add(uuid.toString());

        String content = gson.toJson(json);

        try {
            Files.writeString(file.toPath(), content, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void removeUserMinecraftId(long user, @NotNull UUID uuid) throws DatabaseException {
        File file = new File(dir, user + ".json");

        if (!file.exists())
            throw new NoSuchEntryException();

        JsonObject json;

        try {
            String content = Files.readString(file.toPath());
            json = gson.fromJson(content, JsonObject.class);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }

        JsonArray minecraft = json.getAsJsonArray("minecraft");
        minecraft.remove(new JsonPrimitive(uuid.toString()));

        String content = gson.toJson(json);

        try {
            Files.writeString(file.toPath(), content, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void deleteUser(long id) throws DatabaseException {
        File oldFile = new File(dir, id + ".json");
        File newFile = new File(dir, id + ".json.old");

        if (!oldFile.exists()) return;

        oldFile.renameTo(newFile);
    }
}
