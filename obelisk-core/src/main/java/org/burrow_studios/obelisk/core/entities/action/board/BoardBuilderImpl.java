package org.burrow_studios.obelisk.core.entities.action.board;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.board.BoardBuilder;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.BuilderImpl;
import org.burrow_studios.obelisk.core.cache.DelegatingTurtleCacheView;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.checks.board.BoardChecks;
import org.burrow_studios.obelisk.core.entities.impl.GroupImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.BoardImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.IssueImpl;
import org.burrow_studios.obelisk.core.entities.impl.board.TagImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

import static org.burrow_studios.obelisk.core.entities.BuildHelper.buildDelegatingCacheView;

public class BoardBuilderImpl extends BuilderImpl<Board> implements BoardBuilder {
    public BoardBuilderImpl(@NotNull ObeliskImpl api) {
        super(
                api,
                Board.class,
                Route.Board.CREATE.builder().compile(),
                BoardBuilderImpl::build
        );
    }

    protected static @NotNull BoardImpl build(@NotNull EntityData data, @NotNull ObeliskImpl api) {
        final JsonObject json = data.toJson();

        final long   id      = json.get("id").getAsLong();
        final String title   = json.get("title").getAsString();

        final long groupId = json.get("group").getAsLong();
        final GroupImpl group = api.getGroup(groupId);
        if (group == null)
            throw new IllegalStateException("The group id could not be mapped to a cached group");

        final DelegatingTurtleCacheView<TagImpl> availableTags = buildDelegatingCacheView(json, "tags", api.getTags(), TagImpl.class);
        final DelegatingTurtleCacheView<IssueImpl> issues = buildDelegatingCacheView(json, "issues", api.getIssues(), IssueImpl.class);

        final BoardImpl board = new BoardImpl(api, id, title, group, availableTags, issues);

        api.getBoards().add(board);
        return board;
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
