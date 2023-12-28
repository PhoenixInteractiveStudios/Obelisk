package org.burrow_studios.obelisk.internal.entities;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ProjectImpl extends TurtleImpl implements Project {
    private final @NotNull String title;
    private final @NotNull Timings timings;
    private final @NotNull State state;
    private final @NotNull Set<Long> userIds;

    public ProjectImpl(
            @NotNull ObeliskImpl api,
            long id,
            @NotNull String title,
            @NotNull Timings timings,
            @NotNull State state,
            @NotNull Set<Long> userIds
    ) {
        super(api, id);
        this.title = title;
        this.timings = timings;
        this.state = state;
        this.userIds = userIds;
    }

    @Override
    public @NotNull String getTitle() {
        return this.title;
    }

    @Override
    public @NotNull Timings getTimings() {
        return this.timings;
    }

    @Override
    public @NotNull State getState() {
        return this.state;
    }

    @Override
    public @NotNull Set<Long> getUserIds() {
        return Set.copyOf(this.userIds);
    }

    @Override
    public @NotNull Set<User> getUsers() {
        // TODO
        return Set.of();
    }
}
