package org.burrow_studios.obelisk.server.db.dedicated;

import org.burrow_studios.obelisk.common.function.ExceptionalRunnable;
import org.burrow_studios.obelisk.common.function.ExceptionalSupplier;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.jetbrains.annotations.NotNull;

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
}
