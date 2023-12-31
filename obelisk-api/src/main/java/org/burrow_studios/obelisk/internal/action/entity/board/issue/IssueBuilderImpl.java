package org.burrow_studios.obelisk.internal.action.entity.board.issue;

import org.burrow_studios.obelisk.api.action.entity.board.issue.IssueBuilder;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.entities.issue.Issue;
import org.burrow_studios.obelisk.api.entities.issue.Tag;
import org.burrow_studios.obelisk.internal.EntityBuilder;
import org.burrow_studios.obelisk.internal.action.BuilderImpl;
import org.burrow_studios.obelisk.internal.data.issue.IssueData;
import org.burrow_studios.obelisk.internal.entities.issue.BoardImpl;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class IssueBuilderImpl extends BuilderImpl<Issue, IssueData> implements IssueBuilder {
    public IssueBuilderImpl(@NotNull BoardImpl board) {
        super(
                board.getAPI(),
                Issue.class,
                Route.Board.Issue.CREATE.builder().withArg(board.getId()).compile(),
                new IssueData(),
                EntityBuilder::buildIssue
        );
    }

    @Override
    public @NotNull IssueBuilderImpl addAssignees(@NotNull User... users) {
        this.data.addAssignees(users);
        return this;
    }

    @Override
    public @NotNull IssueBuilderImpl setTitle(@NotNull String title) {
        this.data.setTitle(title);
        return this;
    }

    @Override
    public @NotNull IssueBuilderImpl setState(@NotNull Issue.State state) {
        this.data.setState(state);
        return this;
    }

    @Override
    public @NotNull IssueBuilderImpl addTags(@NotNull Tag... tags) {
        this.data.addTags(tags);
        return this;
    }
}
