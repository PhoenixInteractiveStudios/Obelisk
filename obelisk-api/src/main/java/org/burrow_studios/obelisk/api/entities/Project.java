package org.burrow_studios.obelisk.api.entities;

import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.burrow_studios.obelisk.internal.entities.ProjectImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Set;

public sealed interface Project extends Turtle permits ProjectImpl {
    @NotNull String getTitle();

    @NotNull Timings getTimings();

    @NotNull State getState();

    @NotNull Set<Long> getUserIds();

    @NotNull TurtleSetView<? extends User> getUsers();

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
