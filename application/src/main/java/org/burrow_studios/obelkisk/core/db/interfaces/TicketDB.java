package org.burrow_studios.obelkisk.core.db.interfaces;

import org.burrow_studios.obelkisk.core.entity.DatabaseTicket;
import org.burrow_studios.obelkisk.core.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TicketDB {
    @NotNull DatabaseTicket createTicket(long channel, @NotNull String title) throws DatabaseException;


    @NotNull List<DatabaseTicket> listTickets() throws DatabaseException;

    @NotNull String getTicketTitle(int id) throws DatabaseException;

    long getTicketChannel(int id) throws DatabaseException;


    void setTicketTitle(int id, @NotNull String title) throws DatabaseException;


    void deleteTicket(int id) throws DatabaseException;
}
