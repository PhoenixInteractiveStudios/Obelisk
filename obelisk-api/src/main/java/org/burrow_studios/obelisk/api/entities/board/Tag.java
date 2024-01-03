package org.burrow_studios.obelisk.api.entities.board;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.board.tag.TagModifier;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.internal.entities.board.TagImpl;
import org.jetbrains.annotations.NotNull;

public sealed interface Tag extends Turtle permits TagImpl {
    @Override
    @NotNull TagModifier modify();

    @Override
    @NotNull DeleteAction<Tag> delete();

    @NotNull Board getBoard();

    @NotNull String getName();
}
