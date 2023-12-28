package org.burrow_studios.obelisk.internal.cache;

import org.burrow_studios.obelisk.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TurtleSet<T extends Turtle> implements Set<T> {
    protected final TurtleCache<? super T> cache;
    protected final Set<Long> ids = ConcurrentHashMap.newKeySet();

    public TurtleSet(@NotNull TurtleCache<? super T> cache) {
        this.cache = cache;

        // TODO: register with cache to mirror modifications in real-time
    }

    @Override
    public int size() {
        return this.ids.size();
    }

    @Override
    public boolean isEmpty() {
        return this.ids.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        if (!(o instanceof Turtle turtle)) return false;
        return this.containsId(turtle.getId());
    }

    public boolean containsId(long id) {
        return this.ids.contains(id);
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public @NotNull Object @NotNull [] toArray() {
        return this.ids.stream()
                .map(this.cache::get)
                .filter(Objects::nonNull)
                .toArray();
    }

    @Override
    public <T1> @NotNull T1 @NotNull [] toArray(@NotNull T1 @NotNull [] a) {
        return this.ids.stream()
                .map(this.cache::get)
                .filter(Objects::nonNull)
                .toList()
                .toArray(a);
    }

    @Override
    public boolean add(T t) {
        this.cache.add(t);
        return this.ids.add(t.getId());
    }

    @Override
    public boolean remove(Object o) {
        return this.ids.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        for (Object o : c)
            if (!this.contains(o))
                return false;
        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        boolean changed = false;
        for (T t : c)
            changed = this.add(t) | changed;
        return changed;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return this.ids.retainAll(
                c.stream()
                        .filter(Turtle.class::isInstance)
                        .map(Turtle.class::cast)
                        .map(Turtle::getId)
                        .toList()
        );
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        boolean changed = false;
        for (Object o : c)
            changed = this.remove(o) | changed;
        return changed;
    }

    @Override
    public void clear() {
        this.ids.clear();
    }
}
