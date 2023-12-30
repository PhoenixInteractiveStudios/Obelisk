package org.burrow_studios.obelisk.api.event;

import org.jetbrains.annotations.Nullable;

public interface UpdateEvent<T> extends Event {
    @Nullable T getOldValue();

    @Nullable T getNewValue();
}
