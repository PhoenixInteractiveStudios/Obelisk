package org.burrow_studios.obelisk.core.net.socket;

import org.burrow_studios.obelisk.core.net.NetworkHandler;
import org.jetbrains.annotations.NotNull;

public class SocketAdapter {
    private final @NotNull NetworkHandler networkHandler;

    public SocketAdapter(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }
}
