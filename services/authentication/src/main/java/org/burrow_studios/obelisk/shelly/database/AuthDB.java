package org.burrow_studios.obelisk.shelly.database;

import org.jetbrains.annotations.NotNull;

public interface AuthDB {
    long[] getActiveSessions(long identity) throws DatabaseException;

    @NotNull String getSocketKey(long session) throws DatabaseException;

    void createSession(long id, long identity, String token) throws DatabaseException;

    void invalidateSession(long id, long identity) throws DatabaseException;

    void invalidateAllSessions(long identity) throws DatabaseException;

    void invalidateIdentityTokenFamily(long subject) throws DatabaseException;

    void createIdentity(long id, long subject) throws DatabaseException;
}
