package org.burrow_studios.obelisk.monolith.action;

import org.burrow_studios.obelisk.core.cache.EntityCache;
import org.burrow_studios.obelisk.core.entities.AbstractEntity;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class DatabaseListAction<E extends AbstractEntity> extends DatabaseAction<EntityCache<E>> {
    private final EntityCache<E> cache;

    protected DatabaseListAction(@NotNull EntityCache<E> cache) {
        super(((ObeliskMonolith) cache.getAPI()));
        this.cache = cache;
    }

    public @NotNull EntityCache<E> getCache() {
        return this.cache;
    }

    public void complete(@NotNull CompletableFuture<EntityCache<E>> future, @NotNull List<? extends E> entities) {
        this.cache.clear();
        for (E entity : entities)
            this.cache.add(entity);
        future.complete(this.getCache());
    }
}
