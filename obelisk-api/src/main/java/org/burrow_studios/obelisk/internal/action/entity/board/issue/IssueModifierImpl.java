package org.burrow_studios.obelisk.internal.action.entity.board.issue;

import org.burrow_studios.obelisk.api.action.entity.board.issue.IssueModifier;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.internal.action.ModifierImpl;
import org.burrow_studios.obelisk.internal.data.board.IssueData;
import org.burrow_studios.obelisk.internal.entities.board.IssueImpl;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class IssueModifierImpl extends ModifierImpl<Issue, IssueImpl, IssueData> implements IssueModifier {
    public IssueModifierImpl(@NotNull IssueImpl issue) {
        super(
                issue,
                Route.Board.Issue.EDIT.builder()
                        .withArg(issue.getBoard().getId())
                        .withArg(issue.getId())
                        .compile(),
                new IssueData(issue.getId()),
                IssueData::new
        );
    }

    @Override
    public @NotNull IssueModifierImpl setTitle(@NotNull String title) {
        this.data.setTitle(title);
        return this;
    }

    @Override
    public @NotNull IssueModifier setState(@NotNull Issue.State state) {
        this.data.setState(state);
        return this;
    }
}
