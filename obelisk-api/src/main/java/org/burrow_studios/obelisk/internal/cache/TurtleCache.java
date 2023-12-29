package org.burrow_studios.obelisk.internal.cache;

import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TurtleCache<T extends Turtle> implements TurtleSetView<T> {
    private final ObeliskImpl api;
    private final ConcurrentHashMap<Long, T> data;
    private final Set<DelegatingTurtleCacheView<? extends T>> delegators;
    private final Object lock = new Object();

    public TurtleCache(@NotNull ObeliskImpl api) {
        this.api = api;
        this.data = new ConcurrentHashMap<>();
        this.delegators = ConcurrentHashMap.newKeySet();
    }

    /* - DELEGATORS - */

    void registerDelegator(@NotNull DelegatingTurtleCacheView<? extends T> delegator) {
        this.delegators.add(delegator);
    }

    boolean forgetDelegator(@NotNull DelegatingTurtleCacheView<? extends T> delegator) {
        return this.delegators.remove(delegator);
    }

    /* - - - */

    @Override
    public @NotNull ObeliskImpl getAPI() {
        return this.api;
    }

    public @NotNull Set<Long> getIdsAsImmutaleSet() {
        return Set.copyOf(this.data.keySet());
    }

    @Override
    public @Nullable T get(long id) {
        synchronized (lock) {
            return this.data.get(id);
        }
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        if (!(o instanceof Turtle turtle)) return false;
        return this.containsId(turtle.getId());
    }

    @Override
    public boolean containsId(long id) {
        synchronized (lock) {
            return this.data.containsKey(id);
        }
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return this.data.values().iterator();
    }

    public boolean add(T t) {
        synchronized (lock) {
            return this.data.put(t.getId(), t) == null;
        }
    }

    public boolean remove(Object o) {
        if (!(o instanceof Turtle turtle)) return false;
        final long id = turtle.getId();
        synchronized (lock) {
            for (DelegatingTurtleCacheView<? extends T> delegator : this.delegators)
                delegator.removeById(id);
            return this.data.remove(id) != null;
        }
    }

    public boolean removeById(long id) {
        synchronized (lock) {
            for (DelegatingTurtleCacheView<? extends T> delegator : this.delegators)
                delegator.removeById(id);
            return this.data.remove(id) != null;
        }
    }

    public boolean addAll(@NotNull Iterable<? extends T> c) {
        boolean changed = false;
        for (T t : c)
            changed = this.add(t) | changed;
        return changed;
    }

    public boolean removeAll(@NotNull Iterable<?> c) {
        boolean changed = false;
        for (Object o : c)
            changed = this.remove(o) | changed;
        return changed;
    }

    public void clear() {
        synchronized (lock) {
            for (DelegatingTurtleCacheView<? extends T> delegator : this.delegators)
                delegator.clear();
            this.data.clear();
        }
    }
}
