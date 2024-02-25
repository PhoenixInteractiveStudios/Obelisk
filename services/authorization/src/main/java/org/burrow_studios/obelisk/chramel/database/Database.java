package org.burrow_studios.obelisk.chramel.database;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Database {
    @NotNull List<String> getIntents(long application) throws DatabaseException;

    boolean hasIntent(long application, @NotNull String intent) throws DatabaseException;

    void addIntent(long application, @NotNull String intent) throws DatabaseException;
}
