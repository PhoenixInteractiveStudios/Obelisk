package org.burrow_studios.obelisk.api.entity;

import org.burrow_studios.obelisk.api.entity.dao.TicketDAO;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Ticket {
    private final int id;
    private final TicketDAO dao;

    public Ticket(int id, @NotNull TicketDAO dao) {
        this.id = id;
        this.dao = dao;
    }

    public int getId() {
        return this.id;
    }

    public long getChannelId() {
        return this.dao.getTicketChannel(this.id);
    }

    public @NotNull List<? extends User> getUsers() {
        return this.dao.getTicketUsers(this.id);
    }

    public void delete() {
        this.dao.deleteTicket(this.id);
    }
}
