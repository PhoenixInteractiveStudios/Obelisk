package org.burrow_studios.obelisk.server.event;

import org.burrow_studios.obelisk.server.ObeliskServer;
import org.jetbrains.annotations.NotNull;

public class EventManager {
    private final ObeliskServer server;

    public EventManager(@NotNull ObeliskServer server) {
        this.server = server;
    }
}
