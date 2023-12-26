package org.burrow_studios.obelisk.internal;

import org.burrow_studios.obelisk.api.Obelisk;
import org.burrow_studios.obelisk.internal.event.EventHandlerImpl;
import org.burrow_studios.obelisk.internal.net.NetworkHandler;
import org.jetbrains.annotations.NotNull;

public class ObeliskImpl implements Obelisk {
    private final EventHandlerImpl eventHandler;
    private final NetworkHandler   networkHandler;

    public ObeliskImpl() {
        this.eventHandler   = new EventHandlerImpl(this);
        this.networkHandler = new NetworkHandler(this);
    }

    public @NotNull EventHandlerImpl getEventHandler() {
        return eventHandler;
    }

    public @NotNull NetworkHandler getNetworkHandler() {
        return networkHandler;
    }
}
