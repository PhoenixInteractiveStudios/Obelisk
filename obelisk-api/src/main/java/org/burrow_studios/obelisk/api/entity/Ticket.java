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

    public @NotNull List<? extends DiscordAccount> getUsers() {
        return this.dao.getTicketUsers(this.id);
    }

    public void addUser(@NotNull DiscordAccount user) {
        this.dao.addTicketUser(this.id, user);
    }

    public void removeUser(@NotNull DiscordAccount user) {
        this.dao.removeTicketUser(this.id, user);
    }

    public void delete() {
        this.dao.deleteTicket(this.id);
    }
}
