package org.burrow_studios.obelisk.server.db.dedicated.user;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserDB {
    static @NotNull UserDB get() {
        return new SQLiteUserDB();
    }

    @NotNull Set<Long> getUserIds() throws DatabaseException;

    @NotNull JsonObject getUser(long id) throws DatabaseException;

    void createUser(long id, @NotNull String name) throws DatabaseException;

    void updateUserName(long id, @NotNull String name) throws DatabaseException;

    void updateUserDiscordIds(long id, @NotNull List<Long> discordIds) throws DatabaseException;

    void updateUserMinecraftIds(long id, @NotNull List<UUID> minecraftIds) throws DatabaseException;
}
