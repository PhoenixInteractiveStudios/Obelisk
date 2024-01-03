package org.burrow_studios.obelisk.api.entities.board;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.board.BoardModifier;
import org.burrow_studios.obelisk.api.action.entity.board.issue.IssueBuilder;
import org.burrow_studios.obelisk.api.action.entity.board.tag.TagBuilder;
import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.internal.entities.board.BoardImpl;
import org.jetbrains.annotations.NotNull;

public sealed interface Board extends Turtle permits BoardImpl {
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
