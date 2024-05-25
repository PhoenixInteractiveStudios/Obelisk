package org.burrow_studios.obelisk.core.event;

import org.burrow_studios.obelisk.api.event.Event;
import org.burrow_studios.obelisk.api.event.EventListener;
import org.burrow_studios.obelisk.api.event.EventManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManagerImpl implements EventManager {
    private static final Logger LOG = LoggerFactory.getLogger(EventManager.class);

    private final Set<EventListener> listener = ConcurrentHashMap.newKeySet();

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void handle(@NotNull Event event) {
        for (EventListener listener : this.listener) {
            executorService.execute(() -> {
                try {
                    listener.onEvent(event);
                } catch (InvocationTargetException e) {
                    LOG.warn("An EventListener had an uncaught exception", e.getTargetException());
                } catch (Exception e) {
                    LOG.warn("An EventListener could not process an event", e);
                }
            });
        }
    }

    @Override
    public @NotNull Set<EventListener> getListeners() {
        return Collections.unmodifiableSet(this.listener);
    }

    @Override
    public void registerListener(@NotNull EventListener listener) {
        this.listener.add(listener);
    }

    @Override
    public void unregisterListener(@NotNull EventListener listener) {
        this.listener.remove(listener);
    }

    public void shutdown() {
        this.executorService.shutdown();
    }
}
