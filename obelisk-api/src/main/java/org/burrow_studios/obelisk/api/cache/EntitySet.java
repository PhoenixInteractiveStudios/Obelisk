package org.burrow_studios.obelisk.api.cache;

import org.burrow_studios.obelisk.api.Obelisk;
import org.burrow_studios.obelisk.api.entities.IEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface EntitySet<E extends IEntity> extends Iterable<E> {
    @NotNull Obelisk getAPI();

    @Nullable E get(long id);

    <E1 extends E> @Nullable E1 get(long id, @NotNull Class<E1> type);

    int size();

    default boolean isEmpty() {
        return this.size() != 0;
    }

    boolean contains(Object o);

    boolean containsId(long id);

    boolean containsAll(@NotNull Collection<?> c);
}
