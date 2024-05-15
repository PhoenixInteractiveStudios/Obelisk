package org.burrow_studios.obelisk.core.cache;

import org.burrow_studios.obelisk.api.cache.EntitySet;
import org.burrow_studios.obelisk.core.entities.AbstractEntity;

interface IEntitySetView<E extends AbstractEntity> extends EntitySet<E> {
    void forget(long id);
}
