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

    @Override
    public final @NotNull JsonObject getGroup(long id) throws DatabaseException {
        return this.wrap(() -> this.getGroup0(id));
    }

    @Override
    public final void createGroup(long id, @NotNull String name, int position) throws DatabaseException {
        this.wrap(() -> this.createGroup0(id, name, position));
    }

    @Override
    public final void updateGroupName(long id, @NotNull String name) throws DatabaseException {
        this.wrap(() -> this.updateGroupName0(id, name));
    }

    @Override
    public final void updateGroupPosition(long id, int position) throws DatabaseException {
        this.wrap(() -> this.updateGroupPosition0(id, position));
    }

    @Override
    public final void addGroupMember(long group, long user) throws DatabaseException {
        this.wrap(() -> this.addGroupMember0(group, user));
    }

    @Override
    public final void removeGroupMember(long group, long user) throws DatabaseException {
        this.wrap(() -> this.removeGroupMember0(group, user));
    }

    @Override
    public final void deleteGroup(long id) throws DatabaseException {
        this.wrap(() -> this.deleteGroup0(id));
    }

    protected abstract @NotNull Set<Long> getGroupIds0() throws SQLException;

    protected abstract @NotNull JsonObject getGroup0(long id) throws SQLException;

    protected abstract void createGroup0(long id, @NotNull String name, int position) throws SQLException;

    protected abstract void updateGroupName0(long id, @NotNull String name) throws SQLException;

    protected abstract void updateGroupPosition0(long id, int position) throws SQLException;

    protected abstract void addGroupMember0(long group, long user) throws SQLException;

    protected abstract void removeGroupMember0(long group, long user) throws SQLException;

    protected abstract void deleteGroup0(long id) throws SQLException;
}
