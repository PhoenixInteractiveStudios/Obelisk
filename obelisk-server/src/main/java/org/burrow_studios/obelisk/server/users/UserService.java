package org.burrow_studios.obelisk.server.users;

import org.burrow_studios.obelisk.server.ObeliskServer;
import org.jetbrains.annotations.NotNull;

public class UserService {
    private final ObeliskServer server;

    public UserService(@NotNull ObeliskServer server) {
        this.server = server;
    }
}
