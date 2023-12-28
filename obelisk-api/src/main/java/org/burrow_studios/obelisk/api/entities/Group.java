package org.burrow_studios.obelisk.api.entities;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface Group extends Turtle {
    @NotNull String getName();

    int getSize();

    @NotNull Set<Long> getMemberIds();

    @NotNull Set<? extends User> getMembers();

    int getPosition();
}
