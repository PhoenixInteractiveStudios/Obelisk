package org.burrow_studios.obelisk.server.moderation;

import org.burrow_studios.obelisk.server.ObeliskServer;
import org.jetbrains.annotations.NotNull;

public class ModerationService {
    private final ObeliskServer server;

    public ModerationService(@NotNull ObeliskServer server) {
        this.server = server;
    }
}
