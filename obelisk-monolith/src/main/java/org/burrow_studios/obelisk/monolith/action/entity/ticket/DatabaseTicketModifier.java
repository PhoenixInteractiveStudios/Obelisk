package org.burrow_studios.obelisk.monolith.action.entity.ticket;

import org.burrow_studios.obelisk.api.action.entity.ticket.TicketModifier;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.monolith.action.DatabaseModifier;
import org.burrow_studios.obelisk.monolith.db.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendTicket;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DatabaseTicketModifier extends DatabaseModifier<Ticket> implements TicketModifier {
    private String title;
    private Ticket.State state;

    public DatabaseTicketModifier(@NotNull BackendTicket entity) {
        super(entity);
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<Ticket> future) throws DatabaseException {
        actionableDatabase.modifyTicket(this);
        future.complete(null);
    }

    @Override
    public @NotNull DatabaseTicketModifier setTitle(@Nullable String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    @Override
    public @NotNull DatabaseTicketModifier setState(@NotNull Ticket.State state) {
        this.state = state;
        return this;
    }

    public Ticket.State getState() {
        return this.state;
    }
}
