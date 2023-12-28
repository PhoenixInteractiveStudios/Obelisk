package org.burrow_studios.obelisk.internal.cache;

import org.burrow_studios.obelisk.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TurtleCache<T extends Turtle> implements Set<T> {
    private final ConcurrentHashMap<Long, T> data;

    public TurtleCache() {
        this.data = new ConcurrentHashMap<>();
    }

    public @Nullable T get(long id) {
        return this.data.get(id);
    }

    @SuppressWarnings("unchecked")
    public <T1> @Nullable T1 get(long id, Class<T1> type) {
        T val = this.get(id);
        if (type.isInstance(val))
            return ((T1) val);
        return null;
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        if (!(o instanceof Turtle turtle)) return false;
        return this.data.containsKey(turtle.getId());
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public @NotNull Object @NotNull [] toArray() {
        return this.data.values().toArray();
    }

    @Override
    public <T1> @NotNull T1 @NotNull [] toArray(@NotNull T1 @NotNull [] a) {
        return this.data.values().toArray(a);
    }

    @Override
    public boolean add(T t) {
        return this.data.put(t.getId(), t) == null;
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof Turtle turtle)) return false;
        return this.data.remove(turtle.getId()) != null;
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
        boolean changed = false;
        for (T e : this.data.values())
            if (!c.contains(e))
                changed = this.data.remove(e.getId(), e) | changed;
        return changed;
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
        this.data.clear();
    }
}
