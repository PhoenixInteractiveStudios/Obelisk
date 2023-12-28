package org.burrow_studios.obelisk.api.entities.issue;

import org.burrow_studios.obelisk.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;

public interface Tag extends Turtle {
    long getBoardId();

    @NotNull Board getBoard();

    @NotNull String getName();
}
