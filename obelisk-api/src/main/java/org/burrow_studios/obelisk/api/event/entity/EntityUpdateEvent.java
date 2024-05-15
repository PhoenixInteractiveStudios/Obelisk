package org.burrow_studios.obelisk.api.event.entity;

import org.burrow_studios.obelisk.api.entities.IEntity;
import org.burrow_studios.obelisk.api.event.UpdateEvent;

public interface EntityUpdateEvent<E extends IEntity, T> extends EntityEvent<E>, UpdateEvent<T> { }
