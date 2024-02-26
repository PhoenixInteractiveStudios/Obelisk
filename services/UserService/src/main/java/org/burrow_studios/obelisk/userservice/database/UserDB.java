package org.burrow_studios.obelisk.userservice.database;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.userservice.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

public interface UserDB {
    @NotNull Set<Long> getUserIds() throws DatabaseException;

    @NotNull JsonObject getUser(long id) throws DatabaseException;

    void createUser(long id, @NotNull String name) throws DatabaseException;

    void updateUserName(long id, @NotNull String name) throws DatabaseException;

    void addUserDiscordId(long user, long snowflake) throws DatabaseException;

    void removeUserDiscordId(long user, long snowflake) throws DatabaseException;

    void addUserMinecraftId(long user, @NotNull UUID uuid) throws DatabaseException;

    void removeUserMinecraftId(long user, @NotNull UUID uuid) throws DatabaseException;

    void deleteUser(long id) throws DatabaseException;
}
