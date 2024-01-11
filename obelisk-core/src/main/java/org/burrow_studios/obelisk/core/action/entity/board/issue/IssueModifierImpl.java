package org.burrow_studios.obelisk.core.action.entity.board.issue;

import org.burrow_studios.obelisk.api.action.entity.board.issue.IssueModifier;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.core.action.ModifierImpl;
import org.burrow_studios.obelisk.core.data.board.IssueData;
import org.burrow_studios.obelisk.core.entities.board.IssueImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
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
