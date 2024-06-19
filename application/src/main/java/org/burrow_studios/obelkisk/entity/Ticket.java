package org.burrow_studios.obelkisk.entity;

import org.burrow_studios.obelkisk.db.interfaces.TicketDB;
import org.jetbrains.annotations.NotNull;

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

    public @NotNull String getTitle() {
        return this.database.getTicketTitle(this.id);
    }

    public long getChannelId() {
        return this.database.getTicketChannel(this.id);
    }

    public void setTitle(@NotNull String title) {
        this.database.setTicketTitle(this.id, title);
    }

    public void delete() {
        this.database.deleteTicket(this.id);
    }
}
