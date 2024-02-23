package org.burrow_studios.obelisk.core.entities.action.board.tag;

import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.board.tag.TagBuilder;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.core.action.BuilderImpl;
import org.burrow_studios.obelisk.core.entities.checks.board.TagChecks;
import org.burrow_studios.obelisk.core.entities.impl.board.BoardImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.TagImpl;
import org.burrow_studios.obelisk.commons.rpc.Endpoints;
import org.jetbrains.annotations.NotNull;

public class TagBuilderImpl extends BuilderImpl<Tag> implements TagBuilder {
    public TagBuilderImpl(@NotNull BoardImpl board) {
        super(
                board.getAPI(),
                Tag.class,
                Endpoints.Board.Tag.CREATE.builder(board.getId()).getPath(),
                TagImpl::new
        );
    }

    @Override
    public @NotNull TagBuilderImpl setName(@NotNull String name) throws IllegalArgumentException {
        TagChecks.checkName(name);
        data.set("name", new JsonPrimitive(name));
        return this;
    }
}
