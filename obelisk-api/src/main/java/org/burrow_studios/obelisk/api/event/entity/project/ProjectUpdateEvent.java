package org.burrow_studios.obelisk.api.event.entity.project;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.event.entity.EntityUpdateEvent;
import org.jetbrains.annotations.NotNull;

public abstract class ProjectUpdateEvent<T> extends ProjectEvent implements EntityUpdateEvent<Project, T> {
    protected final T oldValue;
    protected final T newValue;

    protected ProjectUpdateEvent(long id, @NotNull Project entity, T oldValue, T newValue) {
        super(id, entity);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public T getOldValue() {
        return oldValue;
    }

    @Override
    public T getNewValue() {
        return newValue;
    }
}
