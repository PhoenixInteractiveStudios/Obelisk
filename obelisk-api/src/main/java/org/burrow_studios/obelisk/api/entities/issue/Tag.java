package org.burrow_studios.obelisk.api.entities.issue;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.board.tag.TagModifier;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.internal.entities.issue.TagImpl;
import org.jetbrains.annotations.NotNull;

public sealed interface Tag extends Turtle permits TagImpl {
    @Override
    @NotNull TagModifier modify();

    @Override
    @NotNull DeleteAction<Tag> delete();

    long getBoardId();

    @NotNull Board getBoard();

    @NotNull String getName();
}
