package org.burrow_studios.obelisk.monolith.action.entity.ticket;

import org.burrow_studios.obelisk.api.action.entity.ticket.TicketBuilder;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DatabaseTicketBuilder extends DatabaseBuilder<Ticket> implements TicketBuilder {
    private String title;
    private Ticket.State state;

    public DatabaseTicketBuilder(@NotNull ObeliskMonolith obelisk) {
        super(obelisk);
    }

    @Override
    public @NotNull DatabaseTicketBuilder setTitle(@Nullable String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    @Override
    public @NotNull DatabaseTicketBuilder setState(@NotNull Ticket.State state) {
        this.state = state;
        return this;
    }

    public Ticket.State getState() {
        return this.state;
    }
}
