package org.burrow_studios.obelisk.core.cache;

import org.burrow_studios.obelisk.api.cache.OrderedEntitySet;
import org.burrow_studios.obelisk.core.entities.AbstractEntity;
import org.jetbrains.annotations.NotNull;

public interface MutableOrderedEntitySet<E extends AbstractEntity> extends MutableEntitySet<E>, OrderedEntitySet<E> {
    void add(int index, @NotNull E entity);

    void remove(int index);
}
