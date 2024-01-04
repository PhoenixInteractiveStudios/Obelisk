package org.burrow_studios.obelisk.server.users.db.group;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Set;

class SQLiteGroupDB extends SQLGroupDB {
    @Override
    protected @NotNull Set<Long> getGroupIds0() throws SQLException {
        // TODO
        return null;
    }

    @Override
    protected @NotNull JsonObject getGroup0(long id) throws SQLException {
        // TODO
        return null;
    }

    @Override
    protected void createGroup0(long id, @NotNull String name, int position) throws SQLException {
        // TODO
    }

    @Override
    protected void updateGroupName0(long id, @NotNull String name) throws SQLException {
        // TODO
    }

    @Override
    protected void updateGroupPosition0(long id, int position) throws SQLException {
        // TODO
    }

    @Override
    protected void addGroupMember0(long group, long user) throws SQLException {
        // TODO
    }

    @Override
    protected void removeGroupMember0(long group, long user) throws SQLException {
        // TODO
    }
}
