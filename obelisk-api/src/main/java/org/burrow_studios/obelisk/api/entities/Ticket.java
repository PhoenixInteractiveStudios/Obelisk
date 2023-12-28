package org.burrow_studios.obelisk.api.entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface Ticket extends Turtle {
    @Nullable String getTitle();

    @NotNull State getState();

    @NotNull List<String> getTags();

    @NotNull Set<Long> getUserIds();

    @NotNull Set<User> getUsers();

    enum State {
        OPEN,
        RESOLVED,
        FROZEN,
        CLOSED,
        DRAFT
    }
}
