package org.burrow_studios.obelisk.server.its;

import org.burrow_studios.obelisk.server.ObeliskServer;
import org.burrow_studios.obelisk.server.its.db.BoardDB;
import org.jetbrains.annotations.NotNull;

public class IssueTracker {
    private final ObeliskServer server;

    private final BoardDB database;

    public IssueTracker(@NotNull ObeliskServer server) {
        this.server = server;

        this.database = BoardDB.get(this);
    }

    public @NotNull BoardDB getDatabase() {
        return database;
    }
}
