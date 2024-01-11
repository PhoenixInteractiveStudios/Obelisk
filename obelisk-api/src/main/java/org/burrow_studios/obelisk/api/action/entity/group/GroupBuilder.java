package org.burrow_studios.obelisk.api.action.entity.group;

import org.burrow_studios.obelisk.api.action.Builder;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

public interface GroupBuilder extends Builder<Group> {
    @NotNull GroupBuilder setName(@NotNull String name) throws IllegalArgumentException;

    @NotNull GroupBuilder setPosition(int position) throws IllegalArgumentException;

    @NotNull GroupBuilder addMembers(@NotNull User... users);
}
