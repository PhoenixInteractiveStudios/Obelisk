package org.burrow_studios.obelisk.api.action.entity.board;

import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.board.Board;
import org.jetbrains.annotations.NotNull;

public interface BoardModifier extends Modifier<Board> {
    @NotNull BoardModifier setTitle(@NotNull String title) throws IllegalArgumentException;

    @NotNull BoardModifier setGroup(@NotNull Group group);
}
