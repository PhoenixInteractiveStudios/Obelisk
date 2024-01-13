package org.burrow_studios.obelisk.api.event.entity.board.board;

import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public final class BoardCreateEvent extends BoardEvent implements EntityDeleteEvent<Board> {
    public BoardCreateEvent(long id, @NotNull Board entity) {
        super(id, entity);
    }
}
