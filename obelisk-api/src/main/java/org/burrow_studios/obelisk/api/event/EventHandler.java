package org.burrow_studios.obelisk.api.event;

import org.jetbrains.annotations.NotNull;

public interface EventHandler {
    void registerListener(@NotNull EventListener listener);

    void unregisterListener(@NotNull EventListener listener);

    void handle(@NotNull Event event);
}
