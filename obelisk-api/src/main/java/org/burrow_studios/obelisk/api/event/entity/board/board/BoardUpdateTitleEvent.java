package org.burrow_studios.obelisk.api.event.entity.board.board;

import org.burrow_studios.obelisk.api.entities.board.Board;
import org.jetbrains.annotations.NotNull;

public final class BoardUpdateTitleEvent extends BoardUpdateEvent<String> {
    public BoardUpdateTitleEvent(long id, @NotNull Board entity, @NotNull String oldValue, @NotNull String newValue) {
        super(id, entity, oldValue, newValue);
    }
}
