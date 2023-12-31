package org.burrow_studios.obelisk.api.action.entity.board;

import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.issue.Board;
import org.burrow_studios.obelisk.api.entities.issue.Issue;
import org.burrow_studios.obelisk.api.entities.issue.Tag;
import org.jetbrains.annotations.NotNull;

public interface BoardModifier extends Modifier<Board> {
    @NotNull BoardModifier setTitle(@NotNull String title);

    @NotNull BoardModifier setGroup(@NotNull Group group);

    @NotNull BoardModifier addTags(@NotNull Tag... tags);

    @NotNull BoardModifier removeTags(@NotNull Tag... tags);

    @NotNull BoardModifier addIssues(@NotNull Issue... issues);

    @NotNull BoardModifier removeIssues(@NotNull Issue... issues);
}
