package org.burrow_studios.obelisk.internal.action;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class DeleteActionImpl<T extends Turtle> extends ActionImpl<Void> implements DeleteAction<T> {
    private final @NotNull Class<T> type;
    private final long id;

    public DeleteActionImpl(@NotNull ObeliskImpl api, @NotNull Class<T> type, long id) {
        super(api);
        this.type = type;
        this.id = id;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public @NotNull Class<T> getType() {
        return this.type;
    }

    @Override
    public @NotNull CompletableFuture<Void> submit() {
        // TODO
        return CompletableFuture.completedFuture(null);
    }
}
