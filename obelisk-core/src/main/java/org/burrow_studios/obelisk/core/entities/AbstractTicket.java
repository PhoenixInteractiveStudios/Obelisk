package org.burrow_studios.obelisk.core.entities;

import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.core.AbstractObelisk;
import org.burrow_studios.obelisk.core.cache.OrderedEntitySetView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractTicket extends AbstractEntity implements Ticket {
    private @Nullable String title;
    private @NotNull State state;
    private final @NotNull OrderedEntitySetView<AbstractUser> users;

    protected AbstractTicket(
            @NotNull AbstractObelisk obelisk,
            long id,
            @Nullable String title,
            @NotNull State state,
            @NotNull OrderedEntitySetView<AbstractUser> users
    ) {
        super(obelisk, id);
        this.title = title;
        this.state = state;
        this.users = users;
    }

    @Override
    public final @Nullable String getTitle() {
        return this.title;
    }

    public final void setTitle(@Nullable String title) {
        this.title = title;
    }

    @Override
    public final @NotNull State getState() {
        return this.state;
    }

    public final void setState(@NotNull State state) {
        this.state = state;
    }

    @Override
    public final @NotNull OrderedEntitySetView<AbstractUser> getUsers() {
        return this.users;
    }
}
