package org.burrow_studios.obelkisk.core.entity;

import org.burrow_studios.obelisk.api.entity.Ticket;
import org.burrow_studios.obelkisk.core.db.interfaces.TicketDB;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class DatabaseTicket implements Ticket {
    private final int id;
    private final TicketDB database;

    public DatabaseTicket(int id, @NotNull TicketDB database) {
        this.id = id;
        this.database = database;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public long getChannelId() {
        return this.database.getTicketChannel(this.id);
    }

    @Override
    public @NotNull List<DatabaseUser> getUsers() {
        return this.database.getTicketUsers(this.id);
    }

    @Override
    public void delete() {
        this.database.deleteTicket(this.id);
    }
}
