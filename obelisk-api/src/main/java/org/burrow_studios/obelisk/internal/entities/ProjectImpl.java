package org.burrow_studios.obelisk.internal.entities;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ProjectImpl extends TurtleImpl implements Project {
    private @NotNull String title;
    private @NotNull Timings timings;
    private @NotNull State state;
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

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    @Override
    public @NotNull Timings getTimings() {
        return this.timings;
    }

    public void setTimings(@NotNull Timings timings) {
        this.timings = timings;
    }

    @Override
    public @NotNull State getState() {
        return this.state;
    }

    public void setState(@NotNull State state) {
        this.state = state;
    }

    @Override
    public @NotNull Set<Long> getUserIds() {
        return Set.copyOf(this.userIds);
    }

    public @NotNull Set<Long> getUserIdsMutable() {
        return this.userIds;
    }

    @Override
    public @NotNull Set<UserImpl> getUsers() {
        // TODO
        return Set.of();
    }

    public @NotNull Set<UserImpl> getUsersMutable() {
        // TODO
        return Set.of();
    }
}
