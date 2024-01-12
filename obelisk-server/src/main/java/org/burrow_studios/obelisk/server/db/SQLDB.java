package org.burrow_studios.obelisk.server.db;

import org.burrow_studios.obelisk.server.Main;
import org.burrow_studios.obelisk.util.function.ExceptionalRunnable;
import org.burrow_studios.obelisk.util.function.ExceptionalSupplier;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class SQLDB {
    private final ConcurrentHashMap<String, String> statements = new ConcurrentHashMap<>();
    private final Connection connection;

    protected SQLDB(@NotNull String url, @NotNull String user, @NotNull String pass, @NotNull String tableKey) throws DatabaseException {
        try {
            this.connection = DriverManager.getConnection(url, user, pass);

            executeStatement(tableKey);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private String getStatement(@NotNull String key) throws DatabaseException {
        String stmt = statements.get(key);
        if (stmt != null)
            return stmt;
        stmt = getStatementFromResources("/sql/entities/" + key + ".sql");
        statements.put(key, stmt);
        return stmt;
    }

    protected final @NotNull PreparedStatement prepareStatement(@NotNull String key) throws SQLException, DatabaseException {
        return this.connection.prepareStatement(getStatement(key));
    }

    protected final boolean executeStatement(@NotNull String key) throws SQLException, DatabaseException {
        return this.connection.createStatement().execute(getStatement(key));
    }

    protected final @NotNull ResultSet executeQuery(@NotNull String key) throws SQLException, DatabaseException {
        return this.connection.createStatement().executeQuery(getStatement(key));
    }

    protected final <T> T wrap(@NotNull ExceptionalSupplier<T, SQLException> supplier) throws DatabaseException {
        try {
            return supplier.get();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected final void wrap(@NotNull ExceptionalRunnable<SQLException> runnable) throws DatabaseException {
        try {
            runnable.run();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public synchronized static String getStatementFromResources(@NotNull String resource) throws DatabaseException {
        try {
            URL res = Main.class.getResource(resource);
            if (res == null)
                throw new DatabaseException("Statement does not exist in resources: " + resource);
            return Files.readString(Path.of(res.toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new DatabaseException("Could not load statement from resources: " + resource, e);
        }
    }
}
