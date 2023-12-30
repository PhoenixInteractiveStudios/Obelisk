package org.burrow_studios.obelisk.api.action;

import org.burrow_studios.obelisk.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;

public interface Modifier<T extends Turtle> extends Action<T> {
    @NotNull T getEntity() throws IllegalStateException;
}
