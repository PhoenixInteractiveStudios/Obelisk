package org.burrow_studios.obelisk.server.net;

import org.burrow_studios.obelisk.server.ObeliskServer;
import org.burrow_studios.obelisk.server.net.api.APIHandler;
import org.burrow_studios.obelisk.server.net.socket.EventDispatcher;
import org.jetbrains.annotations.NotNull;

public class NetworkHandler {
    private final ObeliskServer server;

    private final APIHandler      apiHandler;
    private final EventDispatcher eventDispatcher;

    public NetworkHandler(@NotNull ObeliskServer server) {
        this.server = server;

        this.apiHandler      = new APIHandler(this);
        this.eventDispatcher = new EventDispatcher(this);
    }

    public @NotNull APIHandler getApiHandler() {
        return apiHandler;
    }

    public @NotNull EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }
}
