package org.burrow_studios.obelisk.core.entities.action.board;

import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.board.BoardModifier;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.core.action.ModifierImpl;
import org.burrow_studios.obelisk.core.entities.EntityUpdater;
import org.burrow_studios.obelisk.core.entities.checks.board.BoardChecks;
import org.burrow_studios.obelisk.core.entities.impl.board.BoardImpl;
import org.burrow_studios.obelisk.commons.http.Endpoints;
import org.jetbrains.annotations.NotNull;

public class BoardModifierImpl extends ModifierImpl<Board, BoardImpl> implements BoardModifier {
    public BoardModifierImpl(@NotNull BoardImpl board) {
        super(
                board,
                Endpoints.Board.EDIT.builder()
                        .withArg(board.getId())
                        .compile(),
                EntityUpdater::updateBoard
        );
    }

    @Override
    public @NotNull BoardModifierImpl setTitle(@NotNull String title) throws IllegalArgumentException {
        BoardChecks.checkTitle(title);
        data.set("title", new JsonPrimitive(title));
        return this;
    }

    @Override
    public @NotNull BoardModifier setGroup(@NotNull Group group) {
        data.set("group", new JsonPrimitive(group.getId()));
        return this;
    }
}
