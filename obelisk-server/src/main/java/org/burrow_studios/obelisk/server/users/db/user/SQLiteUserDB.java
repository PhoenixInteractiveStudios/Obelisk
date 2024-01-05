package org.burrow_studios.obelisk.server.users.db.user;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;

class SQLiteUserDB extends SQLUserDB {
    @Override
    protected @NotNull Set<Long> getUserIds0() throws SQLException {
        // TODO
        return null;
    }

    @Override
    protected @NotNull JsonObject getUser0(long id) throws SQLException {
        // TODO
        return null;
    }

    @Override
    protected void createUser0(long id, @NotNull String name) throws SQLException {
        // TODO
    }

    @Override
    protected void updateUserName0(long id, @NotNull String name) throws SQLException {
        // TODO
    }

    @Override
    protected void addUserDiscordId0(long user, long snowflake) throws SQLException {
        // TODO
    }

    @Override
    protected void removeUserDiscordId0(long user, long snowflake) throws SQLException {
        // TODO
    }

    @Override
    protected void addUserMinecraftId0(long user, @NotNull UUID uuid) throws SQLException {
        // TODO
    }

    @Override
    protected void removeUserMinecraftId0(long user, @NotNull UUID uuid) throws SQLException {
        // TODO
    }

    @Override
    protected void deleteUser0(long id) throws SQLException {
        // TODO
    }
}
