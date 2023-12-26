package org.burrow_studios.obelisk.server.net.socket;

import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.jetbrains.annotations.NotNull;

public class EventDispatcher {
    private final NetworkHandler networkHandler;

    public EventDispatcher(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }
}
