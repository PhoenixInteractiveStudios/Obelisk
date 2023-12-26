package org.burrow_studios.obelisk.internal.event;

import org.burrow_studios.obelisk.api.event.EventHandler;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.jetbrains.annotations.NotNull;

public class EventHandlerImpl implements EventHandler {
    private final ObeliskImpl api;

    public EventHandlerImpl(@NotNull ObeliskImpl api) {
        this.api = api;
    }
}
