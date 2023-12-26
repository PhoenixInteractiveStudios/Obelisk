package org.burrow_studios.obelisk.internal.net;

import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.jetbrains.annotations.NotNull;

public class NetworkHandler {
    private final ObeliskImpl api;

    public NetworkHandler(@NotNull ObeliskImpl api) {
        this.api = api;
    }
}
