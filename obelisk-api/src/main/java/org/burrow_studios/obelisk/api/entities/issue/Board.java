package org.burrow_studios.obelisk.api.entities.issue;

import org.burrow_studios.obelisk.api.action.CreateAction;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.issue.BoardModifier;
import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.internal.entities.issue.BoardImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public sealed interface Board extends Turtle permits BoardImpl {
    @Override
    @NotNull BoardModifier modify();

    @Override
    @NotNull DeleteAction<Board> delete();

    @NotNull CreateAction<Tag> createTag();

    @NotNull CreateAction<Issue> createIssue();

    @NotNull String getTitle();

    long getGroupId();

    @NotNull Group getGroup();

    @NotNull Set<Long> getAvailableTagIds();

    @NotNull TurtleSetView<? extends Tag> getAvailableTags();

    @NotNull Set<Long> getIssueIds();

    @NotNull TurtleSetView<? extends Issue> getIssues();
}
