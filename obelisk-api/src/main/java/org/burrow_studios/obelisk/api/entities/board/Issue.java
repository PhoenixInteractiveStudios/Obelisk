package org.burrow_studios.obelisk.api.entities.board;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.board.issue.IssueModifier;
import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.internal.entities.board.IssueImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public sealed interface Issue extends Turtle permits IssueImpl {
    @Override
    @NotNull IssueModifier modify();

    @Override
    @NotNull DeleteAction<Issue> delete();

    long getBoardId();

    @NotNull Board getBoard();

    long getAuthorId();

    @NotNull User getAuthor();

    @NotNull Set<Long> getAssigneeIds();

    @NotNull TurtleSetView<? extends User> getAssignees();

    @NotNull String getTitle();

    @NotNull State getState();

    @NotNull Set<Long> getTagIds();

    @NotNull TurtleSetView<? extends Tag> getTags();

    enum State {
        OPEN,
        RESOLVED,
        FROZEN,
        ABANDONED
    }
}
