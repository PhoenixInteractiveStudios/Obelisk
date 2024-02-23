package org.burrow_studios.obelisk.core.entities.action.board;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.board.BoardBuilder;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.BuilderImpl;
import org.burrow_studios.obelisk.core.entities.checks.board.BoardChecks;
import org.burrow_studios.obelisk.core.entities.impl.board.BoardImpl;
import org.burrow_studios.obelisk.commons.rpc.Endpoints;
import org.jetbrains.annotations.NotNull;

public class BoardBuilderImpl extends BuilderImpl<Board> implements BoardBuilder {
    public BoardBuilderImpl(@NotNull ObeliskImpl api) {
        super(
                api,
                Board.class,
                Endpoints.Board.CREATE.builder().getPath(),
                BoardImpl::new
        );
    }

    @Override
    public @NotNull BoardBuilderImpl setTitle(@NotNull String title) throws IllegalArgumentException {
        BoardChecks.checkTitle(title);
        data.set("title", new JsonPrimitive(title));
        return this;
    }

    @Override
    public @NotNull BoardBuilderImpl setGroup(@NotNull Group group) {
        data.set("group", new JsonPrimitive(group.getId()));
        return this;
    }

    @Override
    public @NotNull BoardBuilderImpl addTags(@NotNull Tag... tags) {
        JsonArray arr = new JsonArray();
        for (Tag tag : tags)
            arr.add(tag.getId());
        data.addToArray("tags", arr);
        return this;
    }

    @Override
    public @NotNull BoardBuilderImpl addIssues(@NotNull Issue... issues) {
        JsonArray arr = new JsonArray();
        for (Issue issue : issues)
            arr.add(issue.getId());
        data.addToArray("issues", arr);
        return this;
    }
}
