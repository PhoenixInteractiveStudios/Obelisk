package org.burrow_studios.obelkisk.server.db.interfaces;

import org.burrow_studios.obelkisk.server.entity.DatabaseUser;
import org.burrow_studios.obelkisk.server.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface UserDB {
    @NotNull DatabaseUser createUser(@NotNull String name, @Nullable String pronouns) throws DatabaseException;


    @NotNull List<DatabaseUser> listUsers() throws DatabaseException;

    @NotNull DatabaseUser getUser(long id) throws DatabaseException;

    @NotNull String getUserName(long id) throws DatabaseException;

    @Nullable String getUserPronouns(long id) throws DatabaseException;


    void setUserName(long id, @NotNull String name) throws DatabaseException;

    void setUserPronouns(long id, @Nullable String pronouns) throws DatabaseException;


    void deleteUser(long id) throws DatabaseException;
}
