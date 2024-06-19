package org.burrow_studios.obelkisk.core.entity;

import org.burrow_studios.obelisk.api.entity.Ticket;
import org.burrow_studios.obelkisk.core.db.interfaces.TicketDB;
import org.jetbrains.annotations.NotNull;

public final class DatabaseTicket implements Ticket {
    private final int id;
    private final TicketDB database;

    public DatabaseTicket(int id, @NotNull TicketDB database) {
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
