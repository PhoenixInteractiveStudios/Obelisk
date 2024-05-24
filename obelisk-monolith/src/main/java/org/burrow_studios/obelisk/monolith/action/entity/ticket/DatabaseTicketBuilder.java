package org.burrow_studios.obelisk.monolith.action.entity.ticket;

import org.burrow_studios.obelisk.api.action.entity.ticket.TicketBuilder;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseBuilder;
import org.burrow_studios.obelisk.monolith.db.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendTicket;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DatabaseTicketBuilder extends DatabaseBuilder<Ticket> implements TicketBuilder {
    private String title;
    private Ticket.State state;

    public DatabaseTicketBuilder(@NotNull ObeliskMonolith obelisk) {
        super(obelisk);
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<Ticket> future) throws DatabaseException {
        BackendTicket ticket = actionableDatabase.createTicket(this);
        future.complete(ticket);
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
