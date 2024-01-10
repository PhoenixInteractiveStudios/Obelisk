package org.burrow_studios.obelisk.server.users.db.group;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.Main;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public interface GroupDB {
    static @NotNull GroupDB get() throws DatabaseException {
        try {
            return new FileGroupDB(new File(Main.DIR, "groups"));
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
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
