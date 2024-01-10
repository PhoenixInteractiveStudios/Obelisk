package org.burrow_studios.obelisk.core.event;

import org.burrow_studios.obelisk.api.event.Event;
import org.burrow_studios.obelisk.api.event.EventHandler;
import org.burrow_studios.obelisk.api.event.EventListener;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EventHandlerImpl implements EventHandler {
    private final ObeliskImpl api;
    private final Set<EventListener> listeners;

    public EventHandlerImpl(@NotNull ObeliskImpl api) {
        this.api = api;
        this.listeners = ConcurrentHashMap.newKeySet();
    }

    @Override
    public void registerListener(@NotNull EventListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void unregisterListener(@NotNull EventListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public synchronized void handle(@NotNull Event event) {
        if (!Objects.equals(event.getAPI(), api))
            throw new IllegalArgumentException("Cannot process events from another API instance");

        for (EventListener listener : listeners) {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                // TODO: log
            }
        }
    }
}
