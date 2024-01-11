package org.burrow_studios.obelisk.api.action.entity.board.tag;

import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.jetbrains.annotations.NotNull;

public interface TagModifier extends Modifier<Tag> {
    @NotNull TagModifier setName(@NotNull String name) throws IllegalArgumentException;
}
