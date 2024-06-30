package org.burrow_studios.obelkisk.core.db.interfaces;

import org.burrow_studios.obelkisk.core.entity.Ticket;
import org.burrow_studios.obelkisk.core.entity.User;
import org.burrow_studios.obelkisk.core.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TicketDB {
    @NotNull Ticket createTicket(long channel) throws DatabaseException;


    @NotNull List<Ticket> listTickets() throws DatabaseException;

    @NotNull Ticket getTicket(int id) throws DatabaseException;

    long getTicketChannel(int id) throws DatabaseException;

    @NotNull List<User> getTicketUsers(int id) throws DatabaseException;


    void deleteTicket(int id) throws DatabaseException;
}
