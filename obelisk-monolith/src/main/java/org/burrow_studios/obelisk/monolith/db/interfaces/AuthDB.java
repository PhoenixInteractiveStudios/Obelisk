package org.burrow_studios.obelisk.monolith.db.interfaces;

import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;

public interface AuthDB extends Closeable {
    long[] getActiveSessions(long identity) throws DatabaseException;

    @NotNull String getSessionToken(long session) throws DatabaseException;

    long getIdentitySubject(long identity) throws DatabaseException;

    void createSession(long id, long identity, String token) throws DatabaseException;

    void invalidateSession(long id, long identity) throws DatabaseException;

    void invalidateAllSessions(long identity) throws DatabaseException;

    void invalidateIdentityTokenFamily(long subject) throws DatabaseException;

    void createIdentity(long id, long subject) throws DatabaseException;
}
