package org.burrow_studios.obelisk.internal.action.entity.board;

import org.burrow_studios.obelisk.api.action.entity.board.BoardBuilder;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.internal.EntityBuilder;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.action.BuilderImpl;
import org.burrow_studios.obelisk.internal.data.board.BoardData;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class BoardBuilderImpl extends BuilderImpl<Board, BoardData> implements BoardBuilder {
    public BoardBuilderImpl(@NotNull ObeliskImpl api) {
        super(
                api,
                Board.class,
                Route.Board.CREATE.builder().compile(),
                new BoardData(),
                EntityBuilder::buildBoard
        );
    }

    @Override
    public @NotNull BoardBuilderImpl setTitle(@NotNull String title) {
        this.data.setTitle(title);
        return this;
    }

    @Override
    public @NotNull BoardBuilderImpl setGroup(@NotNull Group group) {
        this.data.setGroup(group);
        return this;
    }

    @Override
    public @NotNull BoardBuilderImpl addTags(@NotNull Tag... tags) {
        this.data.addTags(tags);
        return this;
    }

    @Override
    public @NotNull BoardBuilderImpl addIssues(@NotNull Issue... issues) {
        this.data.addIssues(issues);
        return this;
    }
}
