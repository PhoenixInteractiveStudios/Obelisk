package org.burrow_studios.obelisk.internal.action;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.Obelisk;
import org.burrow_studios.obelisk.api.action.CreateAction;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class CreateActionImpl<T extends Turtle> implements CreateAction<T> {
    private final @NotNull ObeliskImpl api;
    private final @NotNull Class<T> type;
    private final @NotNull JsonObject json;

    public CreateActionImpl(@NotNull ObeliskImpl api, @NotNull Class<T> type, @NotNull JsonObject json) {
        this.api = api;
        this.type = type;
        this.json = json;
    }

    @Override
    public @NotNull Obelisk getAPI() {
        return this.api;
    }

    @Override
    public void queue() {
        // TODO
    }

    @Override
    public @NotNull CompletableFuture<T> submit() {
        // TODO
        return null;
    }
}
