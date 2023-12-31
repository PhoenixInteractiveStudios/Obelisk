package org.burrow_studios.obelisk.api.action.entity.group;

import org.burrow_studios.obelisk.api.action.Builder;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

public interface GroupBuilder extends Builder<Group> {
    @NotNull GroupBuilder setName(@NotNull String name);

    @NotNull GroupBuilder setPosition(int position);

    @NotNull GroupBuilder addMembers(@NotNull User... users);
}
