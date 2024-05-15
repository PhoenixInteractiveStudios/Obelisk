package org.burrow_studios.obelisk.core.cache;

import org.burrow_studios.obelisk.api.cache.EntitySet;
import org.burrow_studios.obelisk.core.AbstractObelisk;
import org.burrow_studios.obelisk.core.entities.AbstractEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class EntityCache<E extends AbstractEntity> implements EntitySet<E>, MutableEntitySet<E> {
    private final @NotNull AbstractObelisk obelisk;
    private final @NotNull Class<E> type;

    private final ConcurrentHashMap<Long, E> data;
    private final Set<IEntitySetView<?>> views;

    public EntityCache(@NotNull AbstractObelisk obelisk, @NotNull Class<E> type) {
        this.obelisk = obelisk;
        this.type = type;

        this.data = new ConcurrentHashMap<>();
        this.views = Collections.newSetFromMap(Collections.synchronizedMap(new WeakHashMap<>()));
    }

    void registerView(@NotNull IEntitySetView<? extends E> view) {
        this.views.add(view);
    }

    @Override
    public @NotNull AbstractObelisk getAPI() {
        return this.obelisk;
    }

    @Override
    public @Nullable E get(long id) {
        return this.data.get(id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E1 extends E> @Nullable E1 get(long id, @NotNull Class<E1> type) {
        E e = this.get(id);
        if (type.isInstance(e))
            return ((E1) e);
        return null;
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean contains(Object o) {
        if (o == null) return false;
        if (!type.isInstance(o)) return false;
        return this.data.containsValue(o);
    }

    @Override
    public boolean containsId(long id) {
        return this.data.containsKey(id);
    }

    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean containsAll(@NotNull Collection<?> c) {
        return this.data.values().containsAll(c);
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return this.data.values().iterator();
    }

    @Override
    public void add(@NotNull E entity) {
        this.data.put(entity.getId(), entity);
    }

    @Override
    public void remove(@NotNull E entity) {
        this.views.forEach(view -> view.forget(entity.getId()));
        this.data.remove(entity.getId(), entity);
    }

    @Override
    public void remove(long id) {
        this.views.forEach(view -> view.forget(id));
        this.data.remove(id);
    }
}
