package org.burrow_studios.obelkisk.db.interfaces;

import org.burrow_studios.obelkisk.entity.Ticket;
import org.burrow_studios.obelkisk.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TicketDB {
    @NotNull Ticket createTicket(@NotNull String title) throws DatabaseException;


    @NotNull List<Ticket> listTickets() throws DatabaseException;

    @NotNull String getTicketTitle(long id) throws DatabaseException;


    void setTicketTitle(long id, @NotNull String title) throws DatabaseException;


    void deleteTicket(long id) throws DatabaseException;
}
