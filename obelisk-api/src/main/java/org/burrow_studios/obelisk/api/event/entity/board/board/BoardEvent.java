package org.burrow_studios.obelisk.api.event.entity.board.board;

import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.api.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract sealed class BoardEvent extends EntityEvent<Board> permits BoardCreateEvent, BoardDeleteEvent, BoardUpdateEvent {
    protected BoardEvent(long id, @NotNull Board entity) {
        super(id, entity);
    }
}
