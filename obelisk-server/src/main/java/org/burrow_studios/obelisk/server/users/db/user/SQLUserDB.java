package org.burrow_studios.obelisk.server.users.db.user;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.SQLDB;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;

abstract class SQLUserDB extends SQLDB implements UserDB {
    @Override
    public final @NotNull Set<Long> getUserIds() throws DatabaseException {
        return this.wrap(this::getUserIds0);
    }

    @Override
    public final @NotNull JsonObject getUser(long id) throws DatabaseException {
        return this.wrap(() -> this.getUser0(id));
    }

    @Override
    public final void createUser(long id, @NotNull String name) throws DatabaseException {
        this.wrap(() -> this.createUser0(id, name));
    }

    @Override
    public final void updateUserName(long id, @NotNull String name) throws DatabaseException {
        this.wrap(() -> this.updateUserName0(id, name));
    }

    @Override
    public final void addUserDiscordId(long user, long snowflake) throws DatabaseException {
        this.wrap(() -> this.addUserDiscordId0(user, snowflake));
    }

    @Override
    public final void removeUserDiscordId(long user, long snowflake) throws DatabaseException {
        this.wrap(() -> this.removeUserDiscordId0(user, snowflake));
    }

    @Override
    public final void addUserMinecraftId(long user, @NotNull UUID uuid) throws DatabaseException {
        this.wrap(() -> this.addUserMinecraftId0(user, uuid));
    }

    @Override
    public final void removeUserMinecraftId(long user, @NotNull UUID uuid) throws DatabaseException {
        this.wrap(() -> this.removeUserMinecraftId0(user, uuid));
    }

    protected abstract @NotNull Set<Long> getUserIds0() throws SQLException;

    protected abstract @NotNull JsonObject getUser0(long id) throws SQLException;

    protected abstract void createUser0(long id, @NotNull String name) throws SQLException;

    protected abstract void updateUserName0(long id, @NotNull String name) throws SQLException;

    protected abstract void addUserDiscordId0(long user, long snowflake) throws SQLException;

    protected abstract void removeUserDiscordId0(long user, long snowflake) throws SQLException;

    protected abstract void addUserMinecraftId0(long user, @NotNull UUID uuid) throws SQLException;

    protected abstract void removeUserMinecraftId0(long user, @NotNull UUID uuid) throws SQLException;
}
