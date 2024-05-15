package org.burrow_studios.obelisk.api.event.entity;

import org.burrow_studios.obelisk.api.entities.IEntity;

public interface EntityCreateEvent<E extends IEntity> extends EntityEvent<E> { }
