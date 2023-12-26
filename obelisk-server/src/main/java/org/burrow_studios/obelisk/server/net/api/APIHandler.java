package org.burrow_studios.obelisk.server.net.api;

import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.jetbrains.annotations.NotNull;

public class APIHandler {
    private final NetworkHandler networkHandler;

    public APIHandler(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }
}
