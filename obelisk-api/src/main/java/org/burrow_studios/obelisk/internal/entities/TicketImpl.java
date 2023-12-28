package org.burrow_studios.obelisk.internal.entities;

import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class TicketImpl extends TurtleImpl implements Ticket {
    private @Nullable String title;
    private @NotNull State state;
    private final @NotNull List<String> tags;
    private final @NotNull Set<Long> userIds;

    public TicketImpl(
            @NotNull ObeliskImpl api,
            long id,
            @Nullable String title,
            @NotNull State state,
            @NotNull List<String> tags,
            @NotNull Set<Long> userIds
    ) {
        super(api, id);
        this.title = title;
        this.state = state;
        this.tags = tags;
        this.userIds = userIds;
    }

    @Override
    public @Nullable String getTitle() {
        return this.title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @Override
    public @NotNull State getState() {
        return this.state;
    }

    public void setState(@NotNull State state) {
        this.state = state;
    }

    @Override
    public @NotNull List<String> getTags() {
        return List.copyOf(this.tags);
    }

    public @NotNull List<String> getTagsMutable() {
        return this.tags;
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
