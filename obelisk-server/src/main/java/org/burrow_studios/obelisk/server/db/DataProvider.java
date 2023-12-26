package org.burrow_studios.obelisk.server.db;

import org.burrow_studios.obelisk.server.ObeliskServer;
import org.jetbrains.annotations.NotNull;

public class DataProvider {
    private final ObeliskServer server;

    public DataProvider(@NotNull ObeliskServer server) {
        this.server = server;
    }
}
