package org.burrow_studios.obelisk.api.event;

import org.burrow_studios.obelisk.api.Obelisk;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractEvent implements Event {
    protected final @NotNull Obelisk api;

    protected AbstractEvent(@NotNull Obelisk api) {
        this.api = api;
    }

    @Override
    public final @NotNull Obelisk getAPI() {
        return this.api;
    }
}
