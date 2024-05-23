package org.burrow_studios.obelisk.core.cache;

import org.burrow_studios.obelisk.api.cache.EntitySet;
import org.burrow_studios.obelisk.core.entities.AbstractEntity;
import org.jetbrains.annotations.NotNull;

public interface MutableEntitySet<E extends AbstractEntity> extends EntitySet<E> {
    void add(@NotNull E entity);

    void remove(@NotNull E entity);

    void remove(long id);

    void clear();
}
