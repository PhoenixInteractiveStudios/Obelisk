package org.burrow_studios.obelisk.server.db.dedicated.group;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Set;

abstract class SQLGroupDB implements GroupDB {
    @Override
    public final @NotNull Set<Long> getGroupIds() throws DatabaseException {
        try {
            return this.getGroupIds0();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract @NotNull Set<Long> getGroupIds0() throws SQLException;

    @Override
    public final @NotNull JsonObject getGroup(long id) throws DatabaseException {
        try {
            return this.getGroup0(id);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract @NotNull JsonObject getGroup0(long id) throws SQLException;

    @Override
    public final void createGroup(long id, @NotNull String name, int position) throws DatabaseException {
        try {
            this.createGroup0(id, name, position);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void createGroup0(long id, @NotNull String name, int position) throws SQLException;

    @Override
    public final void updateGroupName(long id, @NotNull String name) throws DatabaseException {
        try {
            this.updateGroupName0(id, name);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void updateGroupName0(long id, @NotNull String name) throws SQLException;

    @Override
    public final void updateGroupPosition(long id, int position) throws DatabaseException {
        try {
            this.updateGroupPosition0(id, position);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void updateGroupPosition0(long id, int position) throws SQLException;

    @Override
    public final void addGroupMember(long group, long user) throws DatabaseException {
        try {
            this.addGroupMember0(group, user);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void addGroupMember0(long group, long user) throws SQLException;

    @Override
    public final void removeGroupMember(long group, long user) throws DatabaseException {
        try {
            this.removeGroupMember0(group, user);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void removeGroupMember0(long group, long user) throws SQLException;
}
