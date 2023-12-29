package org.burrow_studios.obelisk.api.event.entity.issue.board;

import org.burrow_studios.obelisk.api.entities.issue.Board;
import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public final class BoardDeleteEvent extends BoardEvent implements EntityDeleteEvent<Board> {
    public BoardDeleteEvent(@NotNull Board entity) {
        super(entity);
    }
}
