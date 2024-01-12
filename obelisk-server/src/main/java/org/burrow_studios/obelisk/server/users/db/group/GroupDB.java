package org.burrow_studios.obelisk.server.users.db.group;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface GroupDB {
    static @NotNull GroupDB get() throws DatabaseException {
        final String host     = "null";
        final int    port     = 3306;
        final String database = "null";
        final String user     = "null";
        final String pass     = "null";

        return new MySQLGroupDB(host, port, database, user, pass);
    }

    @NotNull Set<Long> getGroupIds() throws DatabaseException;

    @NotNull JsonObject getGroup(long id) throws DatabaseException;

    void createGroup(long id, @NotNull String name, int position) throws DatabaseException;

    void updateGroupName(long id, @NotNull String name) throws DatabaseException;

    void updateGroupPosition(long id, int position) throws DatabaseException;

    void addGroupMember(long group, long user) throws DatabaseException;

    void removeGroupMember(long group, long user) throws DatabaseException;

    void deleteGroup(long id) throws DatabaseException;
}
