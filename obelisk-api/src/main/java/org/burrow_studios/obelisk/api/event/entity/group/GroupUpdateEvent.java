package org.burrow_studios.obelisk.api.event.entity.group;

import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.event.entity.EntityUpdateEvent;
import org.jetbrains.annotations.NotNull;

public abstract sealed class GroupUpdateEvent<T> extends GroupEvent implements EntityUpdateEvent<Group, T> permits GroupUpdateMembersEvent, GroupUpdateNameEvent, GroupUpdatePositionEvent {
    protected final T oldValue;
    protected final T newValue;

    protected GroupUpdateEvent(long id, @NotNull Group entity, T oldValue, T newValue) {
        super(id, entity);
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
