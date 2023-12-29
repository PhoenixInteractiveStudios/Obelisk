package org.burrow_studios.obelisk.api.entities;

import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface Group extends Turtle {
    @NotNull String getName();

    int getSize();

    @NotNull Set<Long> getMemberIds();

    @NotNull TurtleSetView<? extends User> getMembers();

    int getPosition();
}
