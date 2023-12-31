package org.burrow_studios.obelisk.api.action.entity.board.tag;

import org.burrow_studios.obelisk.api.action.Builder;
import org.burrow_studios.obelisk.api.entities.issue.Tag;
import org.jetbrains.annotations.NotNull;

public interface TagBuilder extends Builder<Tag> {
    @NotNull TagBuilder setName(@NotNull String name);
}
