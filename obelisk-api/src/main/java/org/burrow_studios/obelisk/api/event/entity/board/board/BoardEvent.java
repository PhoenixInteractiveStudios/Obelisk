package org.burrow_studios.obelisk.api.event.entity.board.board;

import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.api.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class BoardEvent extends EntityEvent<Board> {
    protected BoardEvent(@NotNull Board entity) {
        super(entity);
    }
}
