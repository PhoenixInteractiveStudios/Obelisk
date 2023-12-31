package org.burrow_studios.obelisk.internal.action.entity.board.tag;

import org.burrow_studios.obelisk.api.action.entity.board.tag.TagBuilder;
import org.burrow_studios.obelisk.api.entities.issue.Tag;
import org.burrow_studios.obelisk.internal.EntityBuilder;
import org.burrow_studios.obelisk.internal.action.BuilderImpl;
import org.burrow_studios.obelisk.internal.data.issue.TagData;
import org.burrow_studios.obelisk.internal.entities.issue.BoardImpl;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class TagBuilderImpl extends BuilderImpl<Tag, TagData> implements TagBuilder {
    public TagBuilderImpl(@NotNull BoardImpl board) {
        super(
                board.getAPI(),
                Tag.class,
                Route.Board.Tag.CREATE.builder().withArg(board.getId()).compile(),
                new TagData(),
                EntityBuilder::buildTag
        );
    }

    @Override
    public @NotNull TagBuilderImpl setName(@NotNull String name) {
        this.data.setName(name);
        return this;
    }
}
