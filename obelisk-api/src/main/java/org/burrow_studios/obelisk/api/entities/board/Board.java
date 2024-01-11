package org.burrow_studios.obelisk.api.entities.board;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.board.BoardModifier;
import org.burrow_studios.obelisk.api.action.entity.board.issue.IssueBuilder;
import org.burrow_studios.obelisk.api.action.entity.board.tag.TagBuilder;
import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;

public interface Board extends Turtle {
    @Override
    @NotNull BoardModifier modify();

    @Override
    @NotNull DeleteAction<Board> delete();

    @NotNull TagBuilder createTag();

    @NotNull IssueBuilder createIssue();

    @NotNull String getTitle();

    @NotNull Group getGroup();

    @NotNull TurtleSetView<? extends Tag> getAvailableTags();

    @NotNull TurtleSetView<? extends Issue> getIssues();
}
