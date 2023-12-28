package org.burrow_studios.obelisk.api.entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Set;

public interface Project extends Turtle {
    @NotNull String getTitle();

    @NotNull Timings getTimings();

    @NotNull State getState();

    @NotNull Set<Long> getUserIds();

    @NotNull Set<User> getUsers();

    record Timings(
            @Nullable Instant release,
            @Nullable Instant apply,
            @Nullable Instant start,
            @Nullable Instant end
    ) { }

    enum State {
        CONCEPT,
        PLANNING,
        APPLICATION,
        RUNNING,
        ENDED,
        STOPPED,
        CANCELED
    }
}
