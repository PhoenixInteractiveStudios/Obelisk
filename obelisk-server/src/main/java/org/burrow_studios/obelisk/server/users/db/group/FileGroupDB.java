package org.burrow_studios.obelisk.server.users.db.group;

import com.google.gson.*;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.JsonFileDatabase;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.Set;

class FileGroupDB implements GroupDB {
    private final JsonFileDatabase database;

    FileGroupDB(@NotNull File dir) throws NotDirectoryException {
        this.database = new JsonFileDatabase(dir);
    }

    @Override
    public @NotNull Set<Long> getGroupIds() throws DatabaseException {
        return database.getIds();
    }

    @Override
    public @NotNull JsonObject getGroup(long id) throws DatabaseException {
        return database.get(id);
    }

    @Override
    public void createGroup(long id, @NotNull String name, int position) throws DatabaseException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("name", name);
        json.addProperty("position", position);
        json.add("members", new JsonArray());

        database.create(id, json);
    }

    @Override
    public void updateGroupName(long id, @NotNull String name) throws DatabaseException {
        database.patch(id, json -> json.addProperty("name", name));
    }

    @Override
    public void updateGroupPosition(long id, int position) throws DatabaseException {
        database.patch(id, json -> json.addProperty("position", position));
    }

    @Override
    public void addGroupMember(long group, long user) throws DatabaseException {
        database.patch(group, json -> {
            JsonArray members = json.getAsJsonArray("members");
            members.add(user);
        });
    }

    @Override
    public void removeGroupMember(long group, long user) throws DatabaseException {
        database.patch(group, json -> {
            JsonArray members = json.getAsJsonArray("members");
            members.remove(new JsonPrimitive(user));
        });
    }

    @Override
    public void deleteGroup(long id) throws DatabaseException {
        database.delete(id);
    }
}
