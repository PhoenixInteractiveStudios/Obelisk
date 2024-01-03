package org.burrow_studios.obelisk.api.event.entity.issue.board;

import org.burrow_studios.obelisk.api.entities.issue.Board;
import org.burrow_studios.obelisk.api.event.entity.EntityUpdateEvent;
import org.jetbrains.annotations.NotNull;

public abstract class BoardUpdateEvent<T> extends BoardEvent implements EntityUpdateEvent<Board, T> {
    protected final T oldValue;
    protected final T newValue;

    protected BoardUpdateEvent(@NotNull Board entity, T oldValue, T newValue) {
        super(entity);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public final T getOldValue() {
        return this.oldValue;
    }

    @Override
    public final T getNewValue() {
        return this.newValue;
    }
}
