package org.burrow_studios.obelisk.api.entities.board;

import org.burrow_studios.obelisk.api.action.Action;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.board.issue.IssueModifier;
import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

public interface Issue extends Turtle {
    @Override
    @NotNull IssueModifier modify();

    @Override
    @NotNull DeleteAction<Issue> delete();

    @NotNull Board getBoard();

    @NotNull User getAuthor();

    @NotNull TurtleSetView<? extends User> getAssignees();

    @NotNull Action<Issue> addAssignee(@NotNull User user);

    @NotNull Action<Issue> removeAssignee(@NotNull User user);

    @NotNull String getTitle();

    @NotNull State getState();

    @NotNull TurtleSetView<? extends Tag> getTags();

    @NotNull Action<Issue> addTag(@NotNull Tag tag);

    @NotNull Action<Issue> removeTag(@NotNull Tag tag);

    enum State {
        OPEN,
        RESOLVED,
        FROZEN,
        ABANDONED
    }
}
