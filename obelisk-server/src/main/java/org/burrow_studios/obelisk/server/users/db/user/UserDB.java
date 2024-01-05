package org.burrow_studios.obelisk.server.users.db.user;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.Main;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Set;
import java.util.UUID;

public interface UserDB {
    static @NotNull UserDB get() {
        return new FileUserDB(new File(Main.DIR, "users"));
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
