package org.burrow_studios.obelisk.api.event.entity.board.board;

import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.api.event.entity.AbstractEntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract sealed class BoardEvent extends AbstractEntityEvent<Board> permits BoardCreateEvent, BoardDeleteEvent, BoardUpdateEvent {
    protected BoardEvent(long id, @NotNull Board entity) {
        super(id, entity);
    }
}
