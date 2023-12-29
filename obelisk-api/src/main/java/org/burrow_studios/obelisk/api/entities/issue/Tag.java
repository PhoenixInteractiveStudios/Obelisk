package org.burrow_studios.obelisk.api.entities.issue;

import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.internal.entities.issue.TagImpl;
import org.jetbrains.annotations.NotNull;

public sealed interface Tag extends Turtle permits TagImpl {
    long getBoardId();

    @NotNull Board getBoard();

    @NotNull String getName();
}
