package org.burrow_studios.obelisk.api.event;

import org.burrow_studios.obelisk.api.Obelisk;
import org.jetbrains.annotations.NotNull;

public interface Event {
    long getId();

    @NotNull Obelisk getAPI();
}
