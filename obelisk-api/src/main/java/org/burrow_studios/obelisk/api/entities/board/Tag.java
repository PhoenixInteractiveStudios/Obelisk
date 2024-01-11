package org.burrow_studios.obelisk.api.entities.board;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.board.tag.TagModifier;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;

public interface Tag extends Turtle {
    @Override
    @NotNull TagModifier modify();

    @Override
    @NotNull DeleteAction<Tag> delete();

    @NotNull Board getBoard();

    @NotNull String getName();
}
