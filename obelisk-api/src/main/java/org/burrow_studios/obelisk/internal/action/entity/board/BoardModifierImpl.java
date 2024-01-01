package org.burrow_studios.obelisk.internal.action.entity.board;

import org.burrow_studios.obelisk.api.action.entity.board.BoardModifier;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.internal.action.ModifierImpl;
import org.burrow_studios.obelisk.internal.data.board.BoardData;
import org.burrow_studios.obelisk.internal.entities.board.BoardImpl;
import org.burrow_studios.obelisk.internal.net.http.Route;
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

    @Override
    public @NotNull BoardModifierImpl addTags(@NotNull Tag... tags) {
        this.data.addTags(tags);
        return this;
    }

    @Override
    public @NotNull BoardModifierImpl removeTags(@NotNull Tag... tags) {
        this.data.removeTags(tags);
        return this;
    }

    @Override
    public @NotNull BoardModifierImpl addIssues(@NotNull Issue... issues) {
        this.data.addIssues(issues);
        return this;
    }

    @Override
    public @NotNull BoardModifierImpl removeIssues(@NotNull Issue... issues) {
        this.data.removeIssues(issues);
        return this;
    }
}
