package org.burrow_studios.obelisk.api.event;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface EventManager {
    void handle(@NotNull Event event);

    @NotNull Set<EventListener> getListeners();

    void registerListener(@NotNull EventListener listener);

    void unregisterListener(@NotNull EventListener listener);
}
