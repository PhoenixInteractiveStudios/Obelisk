package org.burrow_studios.obelisk.monolith.db.interfaces;

import org.burrow_studios.obelisk.monolith.auth.ApplicationData;
import org.burrow_studios.obelisk.monolith.auth.Intent;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface AuthDB extends Closeable {
    @NotNull Set<Intent> getIntents() throws DatabaseException;

    @NotNull Intent getIntent(long id) throws DatabaseException;

    @NotNull Intent getIntent(@NotNull String name) throws DatabaseException;

    @NotNull Map<Long, String> getApplications() throws DatabaseException;

    @NotNull ApplicationData getApplication(long application) throws DatabaseException;

    @NotNull Set<Intent> getApplicationIntents(long application) throws DatabaseException;

    @NotNull String createApplication(@NotNull String name, @NotNull Collection<String> intents) throws DatabaseException;
}
