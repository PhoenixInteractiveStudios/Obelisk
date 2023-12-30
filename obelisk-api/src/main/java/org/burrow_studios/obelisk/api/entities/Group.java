package org.burrow_studios.obelisk.api.entities;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.GroupModifier;
import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.burrow_studios.obelisk.internal.entities.GroupImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public sealed interface Group extends Turtle permits GroupImpl {
    @Override
    @NotNull GroupModifier modify();

    @Override
    @NotNull DeleteAction<Group> delete();

    @NotNull String getName();

    int getSize();

    @NotNull Set<Long> getMemberIds();

    @NotNull TurtleSetView<? extends User> getMembers();

    int getPosition();
}
