package org.burrow_studios.obelisk.server.auth.db;

import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.SQLDB;

import java.sql.SQLException;

public abstract class SQLAuthDB extends SQLDB implements AuthDB {
    @Override
    public final long[] getActiveSessions(long identity) throws DatabaseException {
        return this.wrap(() -> this.getActiveSessions0(identity));
    }

    protected abstract long[] getActiveSessions0(long identity) throws SQLException;

    @Override
    public final void createSession(long id, long identity, String token) throws DatabaseException {
        this.wrap(() -> this.createSession0(id, identity, token));
    }

    protected abstract void createSession0(long id, long identity, String token) throws SQLException;

    @Override
    public final void invalidateSession(long id, long identity) throws DatabaseException {
        this.wrap(() -> this.invalidateSession0(id, identity));
    }

    protected abstract void invalidateSession0(long id, long identity) throws SQLException;

    @Override
    public final void invalidateAllSessions(long identity) throws DatabaseException {
        this.wrap(() -> this.invalidateAllSessions0(identity));
    }

    protected abstract void invalidateAllSessions0(long identity) throws SQLException;

    @Override
    public final void invalidateIdentityTokenFamily(long subject) throws DatabaseException {
        this.wrap(() -> this.invalidateIdentityTokenFamily0(subject));
    }

    protected abstract void invalidateIdentityTokenFamily0(long subject) throws SQLException;

    @Override
    public final void createIdentity(long id, long subject) throws DatabaseException {
        this.wrap(() -> this.createIdentity0(id, subject));
    }

    protected abstract void createIdentity0(long id, long subject) throws SQLException;
}
