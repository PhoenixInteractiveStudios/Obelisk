package org.burrow_studios.obelkisk.server.db.sql;

import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.sql.*;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public final class SQLDB implements Closeable {
    private final ConcurrentHashMap<String, String> statements = new ConcurrentHashMap<>();
    private final Function<String, String> statementSource;

    private final Connection connection;

    public SQLDB(@NotNull String url, @NotNull Function<String, String> statementSource) throws SQLException {
        this.statementSource = statementSource;
        DriverManager.setLoginTimeout(8);
        this.connection = DriverManager.getConnection(url);
    }

    public SQLDB(@NotNull String url, @NotNull String user, @NotNull String pass, @NotNull Function<String, String> statementSource) throws SQLException {
        this.statementSource = statementSource;
        DriverManager.setLoginTimeout(8);
        this.connection = DriverManager.getConnection(url, user, pass);
    }

    public SQLDB(@NotNull String url, @NotNull Properties config, @NotNull Function<String, String> statementSource) throws SQLException {
        this.statementSource = statementSource;
        DriverManager.setLoginTimeout(8);
        this.connection = DriverManager.getConnection(url, config);
    }

    public static @NotNull Function<String, String> resourceFunction(@NotNull Class<?> clazz, @NotNull String basePath) {
        return key -> {
            String resource = basePath + key + ".sql";

            try (InputStream res = clazz.getResourceAsStream(resource)) {
                if (res == null)
                    throw new IOException("Statement does not exist in resources: " + resource);

                return new String(res.readAllBytes());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }

    @Override
    public void close() throws IOException {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    private String getStatement(@NotNull String key) {
        String stmt = statements.get(key);
        if (stmt != null)
            return stmt;
        stmt = statementSource.apply(key);
        statements.put(key, stmt);
        return stmt;
    }

    public @NotNull PreparedStatement preparedStatement(@NotNull String key) throws SQLException {
        return this.connection.prepareStatement(getStatement(key));
    }

    public @NotNull ResultSet executeQuery(@NotNull String key) throws SQLException {
        return this.connection.createStatement().executeQuery(getStatement(key));
    }

    public void execute(@NotNull String key) throws SQLException {
        this.connection.createStatement().execute(getStatement(key));
    }
}
