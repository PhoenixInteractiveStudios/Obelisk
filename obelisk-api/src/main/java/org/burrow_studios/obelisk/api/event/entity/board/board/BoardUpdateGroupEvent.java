package org.burrow_studios.obelisk.api.event.entity.board.board;

import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.api.event.GatewayOpcodes;
import org.jetbrains.annotations.NotNull;

public final class BoardUpdateGroupEvent extends BoardUpdateEvent<Group> {
    public BoardUpdateGroupEvent(long id, @NotNull Board entity, @NotNull Group oldValue, @NotNull Group newValue) {
        super(id, entity, oldValue, newValue);
    }

    @Override
    public int getOpcode() {
        return GatewayOpcodes.BOARD_UPDATE_GROUP_EVENT;
    }
}
