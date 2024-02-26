package org.burrow_studios.obelisk.userservice.database;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.userservice.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface GroupDB {
    @NotNull Set<Long> getGroupIds() throws DatabaseException;

    @NotNull JsonObject getGroup(long id) throws DatabaseException;

    void createGroup(long id, @NotNull String name, int position) throws DatabaseException;

    void updateGroupName(long id, @NotNull String name) throws DatabaseException;

    void updateGroupPosition(long id, int position) throws DatabaseException;

    void addGroupMember(long group, long user) throws DatabaseException;

    void removeGroupMember(long group, long user) throws DatabaseException;

    void deleteGroup(long id) throws DatabaseException;
}
