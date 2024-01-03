package org.burrow_studios.obelisk.api.event.entity.issue.board;

import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.issue.Board;
import org.jetbrains.annotations.NotNull;

public final class BoardUpdateGroupEvent extends BoardUpdateEvent<Group> {
    public BoardUpdateGroupEvent(@NotNull Board entity, @NotNull Group oldValue, @NotNull Group newValue) {
        super(entity, oldValue, newValue);
    }
}
