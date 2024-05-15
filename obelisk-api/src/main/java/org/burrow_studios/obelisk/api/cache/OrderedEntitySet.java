package org.burrow_studios.obelisk.api.cache;

import org.burrow_studios.obelisk.api.entities.IEntity;
import org.jetbrains.annotations.Nullable;

public interface OrderedEntitySet<E extends IEntity> extends EntitySet<E> {
    @Nullable E get(int index);
}
