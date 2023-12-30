package org.burrow_studios.obelisk.internal.action;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.CreateAction;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class CreateActionImpl<T extends Turtle> extends ActionImpl<T> implements CreateAction<T> {
    private final @NotNull Class<T> type;
    private final @NotNull JsonObject json;

    public CreateActionImpl(@NotNull ObeliskImpl api, @NotNull Class<T> type, @NotNull JsonObject json) {
        super(api, route, mapper);
        this.type = type;
        this.json = json;
    }

    @Override
    public @NotNull CompletableFuture<T> submit() {
        // TODO
        return CompletableFuture.completedFuture(null);
    }
}
