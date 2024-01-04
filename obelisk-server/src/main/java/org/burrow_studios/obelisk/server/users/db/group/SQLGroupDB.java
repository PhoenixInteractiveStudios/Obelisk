package org.burrow_studios.obelisk.server.users.db.group;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.SQLDB;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Set;

abstract class SQLGroupDB extends SQLDB implements GroupDB {
    @Override
    public final @NotNull Set<Long> getGroupIds() throws DatabaseException {
        return this.wrap(this::getGroupIds0);
    }

    protected abstract @NotNull Set<Long> getGroupIds0() throws SQLException;

    @Override
    public final @NotNull JsonObject getGroup(long id) throws DatabaseException {
        return this.wrap(() -> this.getGroup0(id));
    }

    protected abstract @NotNull JsonObject getGroup0(long id) throws SQLException;

    @Override
    public final void createGroup(long id, @NotNull String name, int position) throws DatabaseException {
        this.wrap(() -> this.createGroup0(id, name, position));
    }

    protected abstract void createGroup0(long id, @NotNull String name, int position) throws SQLException;

    @Override
    public final void updateGroupName(long id, @NotNull String name) throws DatabaseException {
        this.wrap(() -> this.updateGroupName0(id, name));
    }

    protected abstract void updateGroupName0(long id, @NotNull String name) throws SQLException;

    @Override
    public final void updateGroupPosition(long id, int position) throws DatabaseException {
        this.wrap(() -> this.updateGroupPosition0(id, position));
    }

    protected abstract void updateGroupPosition0(long id, int position) throws SQLException;

    @Override
    public final void addGroupMember(long group, long user) throws DatabaseException {
        this.wrap(() -> this.addGroupMember(group, user));
    }

    protected abstract void addGroupMember0(long group, long user) throws SQLException;

    @Override
    public final void removeGroupMember(long group, long user) throws DatabaseException {
        this.wrap(() -> this.removeGroupMember0(group, user));
    }

    protected abstract void removeGroupMember0(long group, long user) throws SQLException;
}
