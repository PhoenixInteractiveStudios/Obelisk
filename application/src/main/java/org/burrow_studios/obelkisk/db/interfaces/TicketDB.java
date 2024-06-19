package org.burrow_studios.obelkisk.db.interfaces;

import org.burrow_studios.obelkisk.entity.Ticket;
import org.burrow_studios.obelkisk.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TicketDB {
    @NotNull Ticket createTicket(long channel, @NotNull String title) throws DatabaseException;


    @NotNull List<Ticket> listTickets() throws DatabaseException;

    @NotNull String getTicketTitle(int id) throws DatabaseException;

    long getTicketChannel(int id) throws DatabaseException;


    void setTicketTitle(int id, @NotNull String title) throws DatabaseException;


    void deleteTicket(int id) throws DatabaseException;
}
