package org.burrow_studios.obelisk.server.auth;

import org.burrow_studios.obelisk.server.ObeliskServer;
import org.jetbrains.annotations.NotNull;

public class Authenticator {
    private final ObeliskServer server;

    public Authenticator(@NotNull ObeliskServer server) {
        this.server = server;
    }
}
