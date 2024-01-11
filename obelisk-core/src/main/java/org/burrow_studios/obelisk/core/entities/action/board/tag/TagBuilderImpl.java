package org.burrow_studios.obelisk.core.entities.action.board.tag;

import org.burrow_studios.obelisk.api.action.entity.board.tag.TagBuilder;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.core.action.BuilderImpl;
import org.burrow_studios.obelisk.core.entities.data.board.TagData;
import org.burrow_studios.obelisk.core.entities.impl.board.BoardImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.TagImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class TagBuilderImpl extends BuilderImpl<Tag, TagImpl, TagData> implements TagBuilder {
    public TagBuilderImpl(@NotNull BoardImpl board) {
        super(
                board.getAPI(),
                Tag.class,
                Route.Board.Tag.CREATE.builder().withArg(board.getId()).compile(),
                new TagData(),
                TagData::new
        );
    }

    @Override
    public @NotNull TagBuilderImpl setName(@NotNull String name) {
        this.data.setName(name);
        return this;
    }
}
