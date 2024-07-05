package org.burrow_studios.obelisk.api.entity.dao;

import org.burrow_studios.obelisk.api.entity.Ticket;
import org.burrow_studios.obelisk.api.entity.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TicketDAO {
    @NotNull Ticket createTicket(long channel);

    @NotNull List<? extends Ticket> listTickets();
    @NotNull Ticket getTicket(int id);
    long getTicketChannel(int id);
    @NotNull List<? extends User> getTicketUsers(int id);

    void deleteTicket(int id);
}
