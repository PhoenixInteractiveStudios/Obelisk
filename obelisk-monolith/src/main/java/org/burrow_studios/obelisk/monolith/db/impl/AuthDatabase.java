package org.burrow_studios.obelisk.monolith.db.impl;

import org.burrow_studios.obelisk.monolith.Main;
import org.burrow_studios.obelisk.monolith.auth.ApplicationData;
import org.burrow_studios.obelisk.monolith.auth.Intent;
import org.burrow_studios.obelisk.monolith.db.SQLDB;
import org.burrow_studios.obelisk.monolith.db.interfaces.AuthDB;
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
import java.util.*;

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
            this.database.execute("table_intents");
            this.database.execute("table_applications");
            this.database.execute("table_app_intents");
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
    public @NotNull Set<Intent> getIntents() throws DatabaseException {
        Set<Intent> intents = new HashSet<>();

        try (PreparedStatement stmt = this.database.preparedStatement("intents_list")) {
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                final long   id          = res.getLong("id");
                final String name        = res.getString("name");
                final String description = res.getString("description");

                Intent intent = new Intent(id, name, description);
                intents.add(intent);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Collections.unmodifiableSet(intents);
    }

    @Override
    public @NotNull Intent getIntent(long id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("intent_get")) {
            stmt.setLong(1, id);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                throw new NoSuchEntryException();

            return new Intent(id,
                    res.getString("name"),
                    res.getString("description")
            );
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    @Override
    public @NotNull Intent getIntent(@NotNull String name) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("intent_get_by_name")) {
            stmt.setString(1, name);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                throw new NoSuchEntryException();

            return new Intent(
                    res.getLong("id"), name,
                    res.getString("description")
            );
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    @Override
    public @NotNull Map<Long, String> getApplications() throws DatabaseException {
        Map<Long, String> map = new LinkedHashMap<>();

        try (PreparedStatement stmt = this.database.preparedStatement("applications_list")) {
            ResultSet res = stmt.executeQuery();

            while (res.next())
                map.put(
                        res.getLong("id"),
                        res.getString("name")
                );
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Collections.unmodifiableMap(map);
    }

    @Override
    public @NotNull ApplicationData getApplication(long application) throws DatabaseException {
        String name;
        String pubKey;

        try (PreparedStatement stmt = this.database.preparedStatement("application_get")) {
            stmt.setLong(1, application);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                throw new NoSuchEntryException();

            name   = res.getString("name");
            pubKey = res.getString("pub_key");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        Set<Intent> intents = this.getApplicationIntents(application);

        try {
            return new ApplicationData(application, name, pubKey, intents);
        } catch (IllegalArgumentException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public @NotNull Set<Intent> getApplicationIntents(long application) throws DatabaseException {
        Set<Intent> intents = new HashSet<>();

        try (PreparedStatement stmt = this.database.preparedStatement("app_intents_list")) {
            stmt.setLong(1, application);

            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                final long   id          = res.getLong("id");
                final String name        = res.getString("name");
                final String description = res.getString("description");

                Intent intent = new Intent(id, name, description);
                intents.add(intent);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Collections.unmodifiableSet(intents);
    }

    @Override
    public @NotNull String createApplication(@NotNull String name, @NotNull Collection<String> intents) throws DatabaseException {
        return null;
    }
}
