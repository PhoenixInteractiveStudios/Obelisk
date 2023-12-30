package org.burrow_studios.obelisk.api.action.entity;

import org.burrow_studios.obelisk.api.action.Modifier;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

public interface GroupModifier extends Modifier<Group> {
    @NotNull GroupModifier setName(@NotNull String name);

    @NotNull GroupModifier setPosition(int position);

    @NotNull GroupModifier addMembers(@NotNull User... users);

    @NotNull GroupModifier removeMembers(@NotNull User... users);
}
