package org.burrow_studios.obelisk.internal.action.entity.board;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.board.BoardBuilder;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.issue.Board;
import org.burrow_studios.obelisk.api.entities.issue.Issue;
import org.burrow_studios.obelisk.api.entities.issue.Tag;
import org.burrow_studios.obelisk.internal.EntityBuilder;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.action.BuilderImpl;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class BoardBuilderImpl extends BuilderImpl<Board> implements BoardBuilder {
    public BoardBuilderImpl(@NotNull ObeliskImpl api) {
        super(
                api,
                Board.class,
                Route.Board.CREATE.builder().compile(),
                EntityBuilder::buildBoard
        );
    }

    @Override
    public @NotNull BoardBuilderImpl setTitle(@NotNull String title) {
        this.set("title", new JsonPrimitive(title));
        return this;
    }

    @Override
    public @NotNull BoardBuilderImpl setGroup(@NotNull Group group) {
        this.set("group", new JsonPrimitive(group.getId()));
        return this;
    }

    @Override
    public @NotNull BoardBuilderImpl addTags(@NotNull Tag... tags) {
        JsonArray arr = new JsonArray();
        for (Tag tag : tags)
            arr.add(tag.getId());
        this.add("tags", arr);
        return this;
    }

    @Override
    public @NotNull BoardBuilderImpl addIssues(@NotNull Issue... issues) {
        JsonArray arr = new JsonArray();
        for (Issue issue : issues)
            arr.add(issue.getId());
        this.add("issues", arr);
        return this;
    }
}
