package org.burrow_studios.obelisk.api.entities;

import org.burrow_studios.obelisk.api.action.Action;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.group.GroupModifier;
import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.burrow_studios.obelisk.internal.entities.GroupImpl;
import org.jetbrains.annotations.NotNull;

public sealed interface Group extends Turtle permits GroupImpl {
    @Override
    @NotNull GroupModifier modify();

    @Override
    @NotNull DeleteAction<Group> delete();

    @NotNull String getName();

    int getSize();

    @NotNull TurtleSetView<? extends User> getMembers();

    @NotNull Action<Group> addMember(@NotNull User user);

    @NotNull Action<Group> removeMember(@NotNull User user);

    int getPosition();
}
