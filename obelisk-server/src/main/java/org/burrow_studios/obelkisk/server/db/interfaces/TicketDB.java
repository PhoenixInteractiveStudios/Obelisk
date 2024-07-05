package org.burrow_studios.obelkisk.server.db.interfaces;

import org.burrow_studios.obelkisk.server.entity.DatabaseTicket;
import org.burrow_studios.obelkisk.server.entity.DatabaseUser;
import org.burrow_studios.obelkisk.server.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TicketDB {
    @NotNull DatabaseTicket createTicket(long channel) throws DatabaseException;


    @NotNull List<DatabaseTicket> listTickets() throws DatabaseException;

    @NotNull DatabaseTicket getTicket(int id) throws DatabaseException;

    long getTicketChannel(int id) throws DatabaseException;

    @NotNull List<DatabaseUser> getTicketUsers(int id) throws DatabaseException;


    void deleteTicket(int id) throws DatabaseException;
}
