package org.burrow_studios.obelisk.internal.entities.board;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.action.DeleteActionImpl;
import org.burrow_studios.obelisk.internal.action.entity.board.BoardModifierImpl;
import org.burrow_studios.obelisk.internal.action.entity.board.issue.IssueBuilderImpl;
import org.burrow_studios.obelisk.internal.action.entity.board.tag.TagBuilderImpl;
import org.burrow_studios.obelisk.internal.cache.TurtleCache;
import org.burrow_studios.obelisk.internal.entities.TurtleImpl;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class BoardImpl extends TurtleImpl<Board> implements Board {
    private @NotNull String title;
    private long groupId;
    private final @NotNull TurtleCache<TagImpl> availableTags;
    private final @NotNull TurtleCache<IssueImpl> issues;

    public BoardImpl(
            @NotNull ObeliskImpl api,
            long id,
            @NotNull String title,
            long groupId,
            @NotNull TurtleCache<TagImpl> availableTags,
            @NotNull TurtleCache<IssueImpl> issues
    ) {
        super(api, id);
        this.title = title;
        this.groupId = groupId;
        this.availableTags = availableTags;
        this.issues = issues;
    }

    @Override
    public @NotNull JsonObject toJson() {
        JsonObject json = super.toJson();
        json.addProperty("title", title);
        json.addProperty("group", groupId);

        JsonArray tagIds = new JsonArray();
        for (long tagId : this.availableTags.getIdsAsImmutaleSet())
            tagIds.add(tagId);
        json.add("tags", tagIds);

        JsonArray issueIds = new JsonArray();
        for (long issueId : this.issues.getIdsAsImmutaleSet())
            issueIds.add(issueId);
        json.add("issues", issueIds);

        return json;
    }

    @Override
    public @NotNull BoardModifierImpl modify() {
        return new BoardModifierImpl(this);
    }

    @Override
    public @NotNull DeleteAction<Board> delete() {
        return new DeleteActionImpl<>(
                this.getAPI(),
                Board.class,
                this.getId(),
                Route.Board.DELETE.builder()
                        .withArg(getId())
                        .compile()
        );
    }

    @Override
    public @NotNull TagBuilderImpl createTag() {
        return new TagBuilderImpl(this);
    }

    @Override
    public @NotNull IssueBuilderImpl createIssue() {
        return new IssueBuilderImpl(this);
    }

    @Override
    public @NotNull String getTitle() {
        return this.title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    @Override
    public long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    @Override
    public @NotNull Group getGroup() {
        // TODO
        return null;
    }

    public void setGroup(@NotNull Group group) {
        this.setGroupId(group.getId());
    }

    @Override
    public @NotNull Set<Long> getAvailableTagIds() {
        return this.availableTags.getIdsAsImmutaleSet();
    }

    @Override
    public @NotNull TurtleCache<TagImpl> getAvailableTags() {
        return this.availableTags;
    }

    @Override
    public @NotNull Set<Long> getIssueIds() {
        return this.issues.getIdsAsImmutaleSet();
    }

    @Override
    public @NotNull TurtleCache<IssueImpl> getIssues() {
        return this.issues;
    }
}
