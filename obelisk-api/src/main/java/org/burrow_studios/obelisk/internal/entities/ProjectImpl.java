package org.burrow_studios.obelisk.internal.entities;

import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.cache.DelegatingTurtleCacheView;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ProjectImpl extends TurtleImpl implements Project {
    private @NotNull String title;
    private @NotNull Timings timings;
    private @NotNull State state;
    private final @NotNull DelegatingTurtleCacheView<UserImpl> users;

    public ProjectImpl(
            @NotNull ObeliskImpl api,
            long id,
            @NotNull String title,
            @NotNull Timings timings,
            @NotNull State state,
            @NotNull DelegatingTurtleCacheView<UserImpl> users
    ) {
        super(api, id);
        this.title = title;
        this.timings = timings;
        this.state = state;
        this.users = users;
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
        return this.users.getIdsAsImmutaleSet();
    }

    @Override
    public @NotNull TurtleSetView<UserImpl> getUsers() {
        return this.users;
    }
}
