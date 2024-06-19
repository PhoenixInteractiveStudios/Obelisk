package org.burrow_studios.obelkisk.entity;

import org.burrow_studios.obelkisk.db.interfaces.TicketDB;
import org.jetbrains.annotations.NotNull;

public final class Ticket extends AbstractEntity {
    private final TicketDB database;

    public Ticket(long id, @NotNull TicketDB database) {
        super(id);
        this.database = database;
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

    @Override
    public void delete() {
        this.database.deleteTicket(this.id);
    }
}
