package org.burrow_studios.obelisk.server.db.dedicated.user;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;
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
    protected void updateUserDiscordIds0(long id, @NotNull List<Long> discordIds) throws SQLException {
        // TODO
    }

    @Override
    protected void updateUserMinecraftIds0(long id, @NotNull List<UUID> minecraftIds) throws SQLException {
        // TODO
    }
}
