package org.burrow_studios.obelisk.core.cache;

import org.burrow_studios.obelisk.api.cache.OrderedEntitySet;
import org.burrow_studios.obelisk.api.entities.IEntity;
import org.burrow_studios.obelisk.core.AbstractObelisk;
import org.burrow_studios.obelisk.core.entities.AbstractEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class OrderedEntitySetView<E extends AbstractEntity> implements OrderedEntitySet<E>, IEntitySetView<E>, MutableOrderedEntitySet<E> {
    private final @NotNull EntityCache<? super E> cache;
    private final @NotNull Class<E> type;

    private final List<Long> data;

    public OrderedEntitySetView(@NotNull EntityCache<? super E> cache, @NotNull Class<E> type) {
        this.cache = cache;
        this.type = type;

        this.data = Collections.synchronizedList(new LinkedList<>());

        this.cache.registerView(this);
    }

    @Override
    public @NotNull AbstractObelisk getAPI() {
        return this.cache.getAPI();
    }

    @Override
    public @Nullable E get(long id) {
        if (this.data.contains(id))
            return this.cache.get(id, type);
        return null;
    }

    @Override
    public <E1 extends E> @Nullable E1 get(long id, @NotNull Class<E1> type) {
        if (this.data.contains(id))
            return this.cache.get(id, type);
        return null;
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        if (!(o instanceof IEntity e)) return false;
        if (!this.type.isInstance(e)) return false;
        return this.data.contains(e.getId());
    }

    @Override
    public boolean containsId(long id) {
        return this.data.contains(id);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        for (Object o : c)
            if (!this.contains(o))
                return false;
        return true;
    }

    @Override
    public @Nullable E get(int index) {
        return this.cache.get(this.data.get(index), type);
    }

    // TODO: somehow use intermediate iterator?
    @Override
    public @NotNull Iterator<E> iterator() {
        return this.data.stream()
                .map(l -> this.cache.get(l, type))
                .iterator();
    }

    @Override
    public void add(@NotNull E entity) {
        this.cache.add(entity);
        this.data.add(entity.getId());
    }

    @Override
    public void remove(@NotNull E entity) {
        this.data.remove(entity.getId());
    }

    @Override
    public void remove(long id) {
        this.data.remove(id);
    }

    @Override
    public void add(int index, @NotNull E entity) {
        this.cache.add(entity);
        this.data.add(index, entity.getId());
    }

    @Override
    public void remove(int index) {
        this.data.remove(index);
    }

    @Override
    public void forget(long id) {
        this.remove(id);
    }

    @Override
    public void clear() {
        this.data.clear();
    }
}
