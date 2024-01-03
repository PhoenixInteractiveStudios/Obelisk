package org.burrow_studios.obelisk.api.action;

import org.burrow_studios.obelisk.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;

public interface DeleteAction<T extends Turtle> extends Action<Void> {
    long getId();

    @NotNull Class<T> getType();
}
