package org.burrow_studios.obelisk.server.users.db.user;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

public interface UserDB {
    static @NotNull UserDB get() throws DatabaseException {
        final String host     = "null";
        final int    port     = 3306;
        final String database = "null";
        final String user     = "null";
        final String pass     = "null";

        return new MySQLUserDB(host, port, database, user, pass);
    }

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
