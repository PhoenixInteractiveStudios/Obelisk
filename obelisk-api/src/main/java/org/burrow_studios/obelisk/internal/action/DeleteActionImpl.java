package org.burrow_studios.obelisk.internal.action;

import org.burrow_studios.obelisk.api.Obelisk;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class DeleteActionImpl<T extends Turtle> implements DeleteAction<T> {
    private final @NotNull ObeliskImpl api;
    private final @NotNull Class<T> type;
    private final long id;

    public DeleteActionImpl(@NotNull ObeliskImpl api, @NotNull Class<T> type, long id) {
        this.api = api;
        this.type = type;
        this.id = id;
    }

    @Override
    public @NotNull Obelisk getAPI() {
        return this.api;
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
    public void queue() {
        // TODO
    }

    @Override
    public @NotNull CompletableFuture<Void> submit() {
        // TODO
        return null;
    }
}
