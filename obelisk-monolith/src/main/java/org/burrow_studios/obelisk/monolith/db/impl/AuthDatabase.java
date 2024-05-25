package org.burrow_studios.obelisk.monolith.db.impl;

import org.burrow_studios.obelisk.monolith.Main;
import org.burrow_studios.obelisk.monolith.db.interfaces.AuthDB;
import org.burrow_studios.obelisk.monolith.db.SQLDB;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.burrow_studios.obelisk.monolith.exceptions.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConfig;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AuthDatabase implements AuthDB {
    private static final Logger LOG = LoggerFactory.getLogger(AuthDB.class);

    private final SQLDB database;

    public AuthDatabase(@NotNull File file) throws DatabaseException {
        String url = String.format("jdbc:sqlite:%s", file.getAbsolutePath());

        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);

        LOG.info("Initializing database connection to {}", url);
        try {
            database = new SQLDB(url, config.toProperties(), SQLDB.resourceFunction(Main.class, "/sql/auth/"));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        LOG.info("Creating tables");
        try {
            this.database.execute("table_identities");
            this.database.execute("table_expired_families");
            this.database.execute("table_sessions");

            this.database.execute("index_application");
            this.database.execute("index_token_family");
        } catch (SQLException e) {
            throw new DatabaseException("Could not create tables", e);
        }

        LOG.info("Database is online");
    }

    @Override
    public void close() throws IOException {
        LOG.warn("Shutting down");
        this.database.close();
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - */

    @Override
    public long[] getActiveSessions(long identity) throws DatabaseException {
        try (PreparedStatement stmt = database.preparedStatement("get_sessions")) {
            stmt.setLong(1, identity);

            ResultSet result = stmt.executeQuery();

            ArrayList<Long> sessionIds = new ArrayList<>();

            while (result.next())
                sessionIds.add(result.getLong("id"));

            return sessionIds.stream()
                    .mapToLong(Long::longValue)
                    .toArray();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public @NotNull String getSessionToken(long session) throws DatabaseException {
        try (PreparedStatement stmt = database.preparedStatement("get_session")) {
            stmt.setLong(1, session);

            ResultSet result = stmt.executeQuery();

            if (!result.next())
                throw new NoSuchEntryException();

            return result.getString("token");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public long getIdentitySubject(long identity) throws DatabaseException {
        try (PreparedStatement stmt = database.preparedStatement("get_application")) {
            stmt.setLong(1, identity);

            ResultSet result = stmt.executeQuery();

            if (!result.next())
                throw new NoSuchEntryException();

            return result.getLong("application");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void createSession(long id, long identity, String token) throws DatabaseException {
        try (PreparedStatement stmt = database.preparedStatement("create_session")) {
            stmt.setLong(1, id);
            stmt.setLong(2, identity);
            stmt.setString(3, token);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void invalidateSession(long id, long identity) throws DatabaseException {
        try (PreparedStatement stmt = database.preparedStatement("expire_session")) {
            stmt.setLong(1, id);
            stmt.setLong(2, identity);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void invalidateAllSessions(long identity) throws DatabaseException {
        try (PreparedStatement stmt = database.preparedStatement("expire_all_sessions")) {
            stmt.setLong(1, identity);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void invalidateIdentityTokenFamily(long application) throws DatabaseException {
        final int family = getCurrentFamily(application, false);

        try (PreparedStatement stmt = database.preparedStatement("create_expired_family")) {
            stmt.setLong(1, application);
            stmt.setInt(2, family);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void createIdentity(long id, long application) throws DatabaseException {
        final int family = getCurrentFamily(application, true);

        try (PreparedStatement stmt = database.preparedStatement("create_identity")) {
            stmt.setLong(1, application);
            stmt.setInt(2, family);
            stmt.setLong(3, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private int getCurrentFamily(long application, boolean createNew) throws DatabaseException {
        int family = 0;

        try (PreparedStatement stmt = database.preparedStatement("get_family")) {
            stmt.setLong(1, application);

            ResultSet result = stmt.executeQuery();

            if (result.next())
                family = result.getInt("family");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        // check if family is expired
        try (PreparedStatement stmt = database.preparedStatement("get_expired_families")) {
            ResultSet result = stmt.executeQuery();

            if (result.next() && createNew)
                family++;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return family;
    }
}
