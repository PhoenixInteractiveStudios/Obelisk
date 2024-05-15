package org.burrow_studios.obelisk.api.event;

public interface UpdateEvent<T> extends Event {
    T getOldValue();

    T getNewValue();
}
