package org.burrow_studios.obelisk.api.cache;

import org.burrow_studios.obelisk.api.Obelisk;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface TurtleSetView<T extends Turtle> extends Iterable<T> {
    @NotNull Obelisk getAPI();

    @Nullable T get(long id);

    @SuppressWarnings("unchecked")
    default <T1 extends T> @Nullable T1 get(long id, Class<T1> type) {
        T val = this.get(id);
        if (type.isInstance(val))
            return (T1) val;
        return null;
    }

    int size();

    default boolean isEmpty() {
        return this.size() == 0;
    }

    boolean contains(Object o);

    boolean containsId(long id);

    default boolean containsAll(@NotNull Collection<?> c) {
        for (Object o : c)
            if (!this.contains(o))
                return false;
        return true;
    }
}
