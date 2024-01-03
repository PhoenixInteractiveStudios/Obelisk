package org.burrow_studios.obelisk.api.event.entity.board.board;

import org.burrow_studios.obelisk.api.entities.board.Board;
import org.jetbrains.annotations.NotNull;

public final class BoardUpdateTitleEvent extends BoardUpdateEvent<String> {
    public BoardUpdateTitleEvent(@NotNull Board entity, @NotNull String oldValue, @NotNull String newValue) {
        super(entity, oldValue, newValue);
    }
}
