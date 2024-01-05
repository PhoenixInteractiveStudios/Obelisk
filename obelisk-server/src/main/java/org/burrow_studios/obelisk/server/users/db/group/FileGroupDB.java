package org.burrow_studios.obelisk.server.users.db.group;

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

class FileGroupDB implements GroupDB {
    private final File dir;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    FileGroupDB(@NotNull File dir) {
        this.dir = dir;
        dir.mkdirs();
    }

    @Override
    public @NotNull Set<Long> getGroupIds() throws DatabaseException {
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
    public @NotNull JsonObject getGroup(long id) throws DatabaseException {
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
    public void createGroup(long id, @NotNull String name, int position) throws DatabaseException {
        File file = new File(dir, id + ".json");

        if (file.exists()) return;

        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("name", name);
        json.addProperty("position", position);
        json.add("members", new JsonArray());

        String content = gson.toJson(json);

        try {
            Files.writeString(file.toPath(), content, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateGroupName(long id, @NotNull String name) throws DatabaseException {
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
    public void updateGroupPosition(long id, int position) throws DatabaseException {
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

        json.addProperty("position", position);

        String content = gson.toJson(json);

        try {
            Files.writeString(file.toPath(), content, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void addGroupMember(long group, long user) throws DatabaseException {
        File file = new File(dir, group + ".json");

        if (!file.exists())
            throw new NoSuchEntryException();

        JsonObject json;

        try {
            String content = Files.readString(file.toPath());
            json = gson.fromJson(content, JsonObject.class);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }

        JsonArray members = json.getAsJsonArray("members");
        members.add(user);

        String content = gson.toJson(json);

        try {
            Files.writeString(file.toPath(), content, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void removeGroupMember(long group, long user) throws DatabaseException {
        File file = new File(dir, group + ".json");

        if (!file.exists())
            throw new NoSuchEntryException();

        JsonObject json;

        try {
            String content = Files.readString(file.toPath());
            json = gson.fromJson(content, JsonObject.class);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }

        JsonArray members = json.getAsJsonArray("members");
        members.remove(new JsonPrimitive(user));

        String content = gson.toJson(json);

        try {
            Files.writeString(file.toPath(), content, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void deleteGroup(long id) throws DatabaseException {
        File oldFile = new File(dir, id + ".json");
        File newFile = new File(dir, id + ".json.old");

        if (!oldFile.exists()) return;

        oldFile.renameTo(newFile);
    }
}
