package org.burrow_studios.obelisk.server.its;

import org.burrow_studios.obelisk.server.ObeliskServer;
import org.jetbrains.annotations.NotNull;

public class IssueTracker {
    private final ObeliskServer server;

    public IssueTracker(@NotNull ObeliskServer server) {
        this.server = server;
    }
}
