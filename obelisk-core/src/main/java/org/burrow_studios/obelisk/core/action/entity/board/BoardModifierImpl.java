package org.burrow_studios.obelisk.core.action.entity.board;

import org.burrow_studios.obelisk.api.action.entity.board.BoardModifier;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.core.action.ModifierImpl;
import org.burrow_studios.obelisk.core.data.board.BoardData;
import org.burrow_studios.obelisk.core.entities.board.BoardImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class BoardModifierImpl extends ModifierImpl<Board, BoardImpl, BoardData> implements BoardModifier {
    public BoardModifierImpl(@NotNull BoardImpl board) {
        super(
                board,
                Route.Board.EDIT.builder()
                        .withArg(board.getId())
                        .compile(),
                new BoardData(board.getId()),
                BoardData::new
        );
    }

    @Override
    public @NotNull BoardModifierImpl setTitle(@NotNull String title) {
        this.data.setTitle(title);
        return this;
    }

    @Override
    public @NotNull BoardModifier setGroup(@NotNull Group group) {
        this.data.setGroup(group);
        return this;
    }
}
