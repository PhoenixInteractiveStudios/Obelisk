package org.burrow_studios.obelisk.api.entities;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.project.ProjectModifier;
import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.burrow_studios.obelisk.internal.entities.ProjectImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Set;

public sealed interface Project extends Turtle permits ProjectImpl {
    @Override
    @NotNull ProjectModifier modify();

    @Override
    @NotNull DeleteAction<Project> delete();

    @NotNull String getTitle();

    @NotNull Timings getTimings();

    @NotNull State getState();

    @NotNull Set<Long> getMemberIds();

    @NotNull TurtleSetView<? extends User> getMembers();

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
