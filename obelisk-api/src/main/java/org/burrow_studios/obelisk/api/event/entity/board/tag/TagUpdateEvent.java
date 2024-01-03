package org.burrow_studios.obelisk.api.event.entity.board.tag;

import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.api.event.entity.EntityUpdateEvent;
import org.jetbrains.annotations.NotNull;

public abstract class TagUpdateEvent<T> extends TagEvent implements EntityUpdateEvent<Tag, T> {
    protected final T oldValue;
    protected final T newValue;

    protected TagUpdateEvent(@NotNull Tag entity, T oldValue, T newValue) {
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
