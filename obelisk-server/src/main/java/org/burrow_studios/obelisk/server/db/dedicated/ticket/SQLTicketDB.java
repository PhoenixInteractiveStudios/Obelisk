package org.burrow_studios.obelisk.server.db.dedicated.ticket;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.Set;

abstract class SQLTicketDB implements TicketDB {
    @Override
    public final @NotNull Set<Long> getTicketIds() throws DatabaseException {
        try {
            return this.getTicketIds0();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract @NotNull Set<Long> getTicketIds0() throws SQLException;

    @Override
    public final @NotNull JsonObject getTicket(long id) throws DatabaseException {
        try {
            return this.getTicket0(id);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract @NotNull JsonObject getTicket0(long id) throws SQLException;

    @Override
    public final void createTicket(long id, @Nullable String title, @NotNull TicketState state) throws DatabaseException {
        try {
            this.createTicket0(id, title, state);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void createTicket0(long id, @Nullable String title, @NotNull TicketState state) throws SQLException;

    @Override
    public final void updateTicketTitle(long id, @Nullable String title) throws DatabaseException {
        try {
            this.updateTicketTitle0(id, title);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void updateTicketTitle0(long id, @Nullable String title) throws SQLException;

    @Override
    public final void updateTicketState(long id, @NotNull TicketState state) throws DatabaseException {
        try {
            this.updateTicketState0(id, state);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void updateTicketState0(long id, @NotNull TicketState state) throws SQLException;

    @Override
    public final void addTicketTag(long ticket, @NotNull String tag) throws DatabaseException {
        try {
            this.addTicketTag0(ticket, tag);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void addTicketTag0(long ticket, @NotNull String tag) throws SQLException;

    @Override
    public final void removeTicketTag(long ticket, @NotNull String tag) throws DatabaseException {
        try {
            this.removeTicketTag0(ticket, tag);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void removeTicketTag0(long ticket, @NotNull String tag) throws SQLException;

    @Override
    public final void addTicketUser(long ticket, long user) throws DatabaseException {
        try {
            this.addTicketUser0(ticket, user);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void addTicketUser0(long ticket, long user) throws SQLException;

    @Override
    public final void removeTicketUser(long ticket, long user) throws DatabaseException {
        try {
            this.removeTicketUser0(ticket, user);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void removeTicketUser0(long ticket, long user) throws SQLException;
}
