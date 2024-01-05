package org.burrow_studios.obelisk.server.users;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.common.TurtleGenerator;
import org.burrow_studios.obelisk.server.ObeliskServer;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.users.db.group.GroupDB;
import org.burrow_studios.obelisk.server.users.db.user.UserDB;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {
    private final ObeliskServer server;

    private final GroupDB groupDB;
    private final  UserDB  userDB;

    private final ConcurrentHashMap<Long, JsonObject> groupCache;
    private final ConcurrentHashMap<Long, JsonObject>  userCache;

    private final TurtleGenerator turtleGenerator;

    public UserService(@NotNull ObeliskServer server) {
        this.server = server;

        this.groupDB = GroupDB.get();
        this.userDB  = UserDB.get();

        this.groupCache = new ConcurrentHashMap<>();
        this.userCache  = new ConcurrentHashMap<>();

        this.turtleGenerator = TurtleGenerator.get("UserService");

        this.groupDB.getGroupIds().forEach(id -> groupCache.put(id, null));
        this.userDB.getUserIds().forEach(id -> userCache.put(id, null));
    }

    public @NotNull Set<Long> getGroups() {
        return Set.copyOf(this.groupCache.keySet());
    }
    
    public @NotNull Set<Long> getUsers() {
        return Set.copyOf(this.userCache.keySet());
    }

    public @NotNull JsonObject getGroup(long id) throws DatabaseException {
        final JsonObject cachedGroup = this.groupCache.get(id);

        if (cachedGroup != null)
            return cachedGroup;

        return this.retrieveGroup(id);
    }

    public @NotNull JsonObject getUser(long id) throws DatabaseException {
        JsonObject cachedUser = this.userCache.get(id);

        if (cachedUser != null)
            return cachedUser;

        return this.retrieveUser(id);
    }

    private @NotNull JsonObject retrieveGroup(long id) throws DatabaseException {
        final JsonObject result = this.groupDB.getGroup(id);
        this.groupCache.put(id, result);
        return result;
    }

    private @NotNull JsonObject retrieveUser(long id) throws DatabaseException {
        final JsonObject result = this.userDB.getUser(id);
        this.userCache.put(id, result);
        return result;
    }

    public @NotNull JsonObject createGroup(@NotNull JsonObject json) throws DatabaseException {
        final long id = this.turtleGenerator.newId();

        final String name     = json.get("name").getAsString();
        final int    position = json.get("position").getAsInt();

        // TODO: validate data

        this.groupDB.createGroup(id, name, position);
        return this.retrieveGroup(id);
    }

    public @NotNull JsonObject createUser(@NotNull JsonObject json) throws DatabaseException {
        final long id = this.turtleGenerator.newId();

        final String name = json.get("name").getAsString();

        // TODO: validate data

        this.userDB.createUser(id, name);
        return this.retrieveUser(id);
    }

    public void deleteGroup(long id) throws DatabaseException {
        this.groupCache.remove(id);
        this.groupDB.deleteGroup(id);
    }

    public void deleteUser(long id) throws DatabaseException {
        this.userCache.remove(id);
        this.userDB.deleteUser(id);
    }

    public void patchGroup(long id, @NotNull JsonObject json) throws DatabaseException {
        Optional.ofNullable(json.get("name"))
                .map(JsonElement::getAsString)
                .ifPresent(name -> groupDB.updateGroupName(id, name));

        Optional.ofNullable(json.get("position"))
                .map(JsonElement::getAsInt)
                .ifPresent(position -> groupDB.updateGroupPosition(id, position));

        this.retrieveGroup(id);
    }

    public void patchUser(long id, @NotNull JsonObject json) throws DatabaseException {
        Optional.ofNullable(json.get("name"))
                .map(JsonElement::getAsString)
                .ifPresent(name -> userDB.updateUserName(id, name));

        JsonObject user = this.getUser(id);

        JsonArray discord = user.getAsJsonArray("discord");
        Optional.ofNullable(json.get("discord"))
                .map(JsonElement::getAsJsonArray)
                .ifPresent(elements -> {
                    // add new ids
                    for (JsonElement element : elements)
                        if (!discord.contains(element))
                            userDB.addUserDiscordId(id, element.getAsLong());
                    // remove old ids
                    for (JsonElement element : discord)
                        if (!elements.contains(element))
                            userDB.removeUserDiscordId(id, element.getAsLong());
                });

        JsonArray minecraft = user.getAsJsonArray("minecraft");
        Optional.ofNullable(json.get("minecraft"))
                .map(JsonElement::getAsJsonArray)
                .ifPresent(elements -> {
                    // add new ids
                    for (JsonElement element : elements)
                        if (!minecraft.contains(element))
                            userDB.addUserMinecraftId(id, UUID.fromString(element.getAsString()));
                    // remove old ids
                    for (JsonElement element : minecraft)
                        if (!elements.contains(element))
                            userDB.removeUserMinecraftId(id, UUID.fromString(element.getAsString()));
                });

        this.retrieveUser(id);
    }

    public void addGroupMember(long group, long user) throws DatabaseException {
        this.groupDB.addGroupMember(group, user);
        this.retrieveGroup(group);
    }

    public void removeGroupMember(long group, long user) throws DatabaseException {
        this.groupDB.removeGroupMember(group, user);
        this.retrieveGroup(group);
    }
}
