package org.burrow_studios.obelisk.api.event.entity.board.board;

import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public final class BoardDeleteEvent extends BoardEvent implements EntityDeleteEvent<Board> {
    public BoardDeleteEvent(long id, @NotNull Board entity) {
        super(id, entity);
    }

    @Override
    public int getOpcode() {
        return 141;
    }
}
