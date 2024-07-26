package org.burrow_studios.obelkisk.entity.dao;

import org.burrow_studios.obelkisk.entity.Ticket;
import org.burrow_studios.obelkisk.entity.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface TicketDAO {
    @NotNull Ticket createTicket(long channel);

    @NotNull List<? extends Ticket> listTickets();
    @NotNull Optional<Ticket> getTicket(int id);
    long getTicketChannel(int id);
    @NotNull List<? extends User> getTicketUsers(int id);

    void addTicketUser(int id, @NotNull User user);
    void removeTicketUser(int id, @NotNull User user);

    void deleteTicket(int id);
}
