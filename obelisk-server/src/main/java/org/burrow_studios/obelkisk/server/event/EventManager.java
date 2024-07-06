package org.burrow_studios.obelkisk.server.event;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public final class EventManager {
    private static final Logger LOG = LoggerFactory.getLogger(EventManager.class);

    private final Set<EventListener> listeners;

    public EventManager() {
        this.listeners = ConcurrentHashMap.newKeySet();
    }

    public void registerListener(@NotNull EventListener listener) {
        this.listeners.add(listener);
    }

    public void unregisterListener(@NotNull EventListener listener) {
        this.listeners.remove(listener);
    }

    public void handle(@NotNull Event event) {
        List<Callable<Boolean>>  checkMethods = new ArrayList<>();
        List<Callable<Void>> executionMethods = new ArrayList<>();
        List<Callable<Void>>   monitorMethods = new ArrayList<>();

        for (EventListener listener : this.listeners) {
            for (Method method : listener.getClass().getMethods()) {
                EventHandler annotation = method.getAnnotation(EventHandler.class);

                // skip if not annotated
                if (annotation == null) continue;

                // skip if too many or too few params
                if (method.getParameterCount() != 1) continue;

                // skip if parameter does not match
                if (!method.getParameters()[0].getType().isInstance(event)) continue;

                if (annotation.stage() == ExecutionStage.CHECK) {
                    if (method.getReturnType().equals(boolean.class)) {
                        // check via exception and return value
                        checkMethods.add(() -> (boolean) method.invoke(listener, event));
                    } else {
                        // check via exception
                        checkMethods.add(() -> {
                            method.invoke(listener, event);
                            return true;
                        });
                    }
                } else if (annotation.stage() == ExecutionStage.EXECUTE) {
                    executionMethods.add(() -> {
                        method.invoke(listener, event);
                        return null;
                    });
                } else if (annotation.stage() == ExecutionStage.MONITOR) {
                    monitorMethods.add(() -> {
                        method.invoke(listener, event);
                        return null;
                    });
                }
            }
        }

        boolean failed = false;

        for (Callable<Boolean> check : checkMethods) {
            try {
                boolean res = check.call();

                if (!res) {
                    LOG.warn("An EventListener CHECK failed");
                    failed = true;
                }
            } catch (Exception e) {
                LOG.warn("An EventListener CHECK failed due to an exception", e);
                failed = true;
            }
        }

        if (failed) return;

        for (Callable<Void> execution : executionMethods) {
            try {
                execution.call();
            } catch (Exception e) {
                LOG.warn("An EventListener EXECUTION failed due to an exception");
                failed = true;
            }
        }

        if (failed) return;

        for (Callable<Void> monitor : monitorMethods) {
            try {
                monitor.call();
            } catch (Exception e) {
                LOG.warn("An EventListener MONITOR failed due to an exception");
            }
        }
    }
}
