package org.burrow_studios.obelisk.server.db.dedicated.user;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.dedicated.SQLDB;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

abstract class SQLUserDB extends SQLDB implements UserDB {
    @Override
    public final @NotNull Set<Long> getUserIds() throws DatabaseException {
        return this.wrap(this::getUserIds0);
    }

    protected abstract @NotNull Set<Long> getUserIds0() throws SQLException;

    @Override
    public final @NotNull JsonObject getUser(long id) throws DatabaseException {
        return this.wrap(() -> this.getUser0(id));
    }

    protected abstract @NotNull JsonObject getUser0(long id) throws SQLException;

    @Override
    public final void createUser(long id, @NotNull String name) throws DatabaseException {
        this.wrap(() -> this.createUser0(id, name));
    }

    protected abstract void createUser0(long id, @NotNull String name) throws SQLException;

    @Override
    public final void updateUserName(long id, @NotNull String name) throws DatabaseException {
        this.wrap(() -> this.updateUserName0(id, name));
    }

    protected abstract void updateUserName0(long id, @NotNull String name) throws SQLException;

    @Override
    public final void updateUserDiscordIds(long id, @NotNull List<Long> discordIds) throws DatabaseException {
        this.wrap(() -> this.updateUserDiscordIds0(id, discordIds));
    }

    protected abstract void updateUserDiscordIds0(long id, @NotNull List<Long> discordIds) throws SQLException;

    @Override
    public final void updateUserMinecraftIds(long id, @NotNull List<UUID> minecraftIds) throws DatabaseException {
        this.wrap(() -> this.updateUserMinecraftIds0(id, minecraftIds));
    }

    protected abstract void updateUserMinecraftIds0(long id, @NotNull List<UUID> minecraftIds) throws SQLException;
}
