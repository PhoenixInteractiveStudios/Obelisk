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
import java.sql.SQLException;

public abstract class SQLDB {
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

    public static String getStatementFromResources(@NotNull String resource) throws DatabaseException {
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
