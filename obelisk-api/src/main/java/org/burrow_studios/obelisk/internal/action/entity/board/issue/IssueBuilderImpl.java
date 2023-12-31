package org.burrow_studios.obelisk.internal.action.entity.board.issue;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.board.issue.IssueBuilder;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.entities.issue.Issue;
import org.burrow_studios.obelisk.api.entities.issue.Tag;
import org.burrow_studios.obelisk.internal.EntityBuilder;
import org.burrow_studios.obelisk.internal.action.BuilderImpl;
import org.burrow_studios.obelisk.internal.entities.issue.BoardImpl;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class IssueBuilderImpl extends BuilderImpl<Issue> implements IssueBuilder {
    public IssueBuilderImpl(@NotNull BoardImpl board) {
        super(
                board.getAPI(),
                Issue.class,
                Route.Board.Issue.CREATE.builder().withArg(board.getId()).compile(),
                EntityBuilder::buildIssue
        );
    }

    @Override
    public @NotNull IssueBuilderImpl addAssignees(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.add("assignees", arr);
        return this;
    }

    @Override
    public @NotNull IssueBuilderImpl setTitle(@NotNull String title) {
        this.set("title", new JsonPrimitive(title));
        return this;
    }

    @Override
    public @NotNull IssueBuilderImpl setState(@NotNull Issue.State state) {
        this.set("state", new JsonPrimitive(state.name()));
        return this;
    }

    @Override
    public @NotNull IssueBuilderImpl addTags(@NotNull Tag... tags) {
        JsonArray arr = new JsonArray();
        for (Tag tag : tags)
            arr.add(tag.getId());
        this.add("tags", arr);
        return this;
    }
}
