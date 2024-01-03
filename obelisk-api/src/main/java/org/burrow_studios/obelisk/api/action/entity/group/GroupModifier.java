package org.burrow_studios.obelisk.api.action.entity.group;

import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.Group;
import org.jetbrains.annotations.NotNull;

public interface GroupModifier extends Modifier<Group> {
    @NotNull GroupModifier setName(@NotNull String name);

    @NotNull GroupModifier setPosition(int position);
}
