package org.burrow_studios.obelisk.api.action.entity.issue;

import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.issue.Tag;
import org.jetbrains.annotations.NotNull;

public interface TagModifier extends Modifier<Tag> {
    @NotNull TagModifier setName(@NotNull String name);
}
