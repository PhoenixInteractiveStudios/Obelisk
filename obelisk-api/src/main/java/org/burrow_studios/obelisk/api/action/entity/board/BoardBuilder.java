package org.burrow_studios.obelisk.api.action.entity.board;

import org.burrow_studios.obelisk.api.action.Builder;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.jetbrains.annotations.NotNull;

public interface BoardBuilder extends Builder<Board> {
    @NotNull BoardBuilder setTitle(@NotNull String title);

    @NotNull BoardBuilder setGroup(@NotNull Group group);

    @NotNull BoardBuilder addTags(@NotNull Tag... tags);

    @NotNull BoardBuilder addIssues(@NotNull Issue... issues);
}
