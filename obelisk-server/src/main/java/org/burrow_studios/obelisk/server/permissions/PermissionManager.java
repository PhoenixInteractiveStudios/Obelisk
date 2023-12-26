package org.burrow_studios.obelisk.server.permissions;

import org.burrow_studios.obelisk.server.ObeliskServer;
import org.jetbrains.annotations.NotNull;

public class PermissionManager {
    private final ObeliskServer server;

    public PermissionManager(@NotNull ObeliskServer server) {
        this.server = server;
    }
}
