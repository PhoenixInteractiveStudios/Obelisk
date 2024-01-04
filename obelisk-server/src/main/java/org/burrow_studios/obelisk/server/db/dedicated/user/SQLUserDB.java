package org.burrow_studios.obelisk.server.db.dedicated.user;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

abstract class SQLUserDB implements UserDB {
    @Override
    public final @NotNull Set<Long> getUserIds() throws DatabaseException {
        try {
            return this.getUserIds0();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract @NotNull Set<Long> getUserIds0() throws SQLException;

    @Override
    public final @NotNull JsonObject getUser(long id) throws DatabaseException {
        try {
            return this.getUser0(id);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract @NotNull JsonObject getUser0(long id) throws SQLException;

    @Override
    public final void createUser(long id, @NotNull String name) throws DatabaseException {
        try {
            this.createUser0(id, name);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void createUser0(long id, @NotNull String name) throws SQLException;

    @Override
    public final void updateUserName(long id, @NotNull String name) throws DatabaseException {
        try {
            this.updateUserName0(id, name);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void updateUserName0(long id, @NotNull String name) throws SQLException;

    @Override
    public final void updateUserDiscordIds(long id, @NotNull List<Long> discordIds) throws DatabaseException {
        try {
            this.updateUserDiscordIds0(id, discordIds);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void updateUserDiscordIds0(long id, @NotNull List<Long> discordIds) throws SQLException;

    @Override
    public final void updateUserMinecraftIds(long id, @NotNull List<UUID> minecraftIds) throws DatabaseException {
        try {
            this.updateUserMinecraftIds0(id, minecraftIds);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void updateUserMinecraftIds0(long id, @NotNull List<UUID> minecraftIds) throws SQLException;
}
