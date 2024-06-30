package org.burrow_studios.obelkisk.core.entity;

import org.burrow_studios.obelkisk.core.db.interfaces.TicketDB;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Ticket {
    private final int id;
    private final TicketDB database;

    public Ticket(int id, @NotNull TicketDB database) {
        this.id = id;
        this.database = database;
    }

    public int getId() {
        return this.id;
    }

    public long getChannelId() {
        return this.database.getTicketChannel(this.id);
    }

    public @NotNull List<User> getUsers() {
        return this.database.getTicketUsers(this.id);
    }

    public void delete() {
        this.database.deleteTicket(this.id);
    }
}
