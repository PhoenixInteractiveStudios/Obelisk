package org.burrow_studios.obelisk.server.moderation.db.ticket;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.SQLDB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.Set;

abstract class SQLTicketDB extends SQLDB implements TicketDB {
    @Override
    public final @NotNull Set<Long> getTicketIds() throws DatabaseException {
        return this.wrap(this::getTicketIds0);
    }

    protected abstract @NotNull Set<Long> getTicketIds0() throws SQLException;

    @Override
    public final @NotNull JsonObject getTicket(long id) throws DatabaseException {
        return this.wrap(() -> this.getTicket0(id));
    }

    protected abstract @NotNull JsonObject getTicket0(long id) throws SQLException;

    @Override
    public final void createTicket(long id, @Nullable String title, @NotNull TicketState state) throws DatabaseException {
        this.wrap(() -> this.createTicket0(id, title, state));
    }

    protected abstract void createTicket0(long id, @Nullable String title, @NotNull TicketState state) throws SQLException;

    @Override
    public final void updateTicketTitle(long id, @Nullable String title) throws DatabaseException {
        this.wrap(() -> this.updateTicketTitle0(id, title));
    }

    protected abstract void updateTicketTitle0(long id, @Nullable String title) throws SQLException;

    @Override
    public final void updateTicketState(long id, @NotNull TicketState state) throws DatabaseException {
        this.wrap(() -> this.updateTicketState0(id, state));
    }

    protected abstract void updateTicketState0(long id, @NotNull TicketState state) throws SQLException;

    @Override
    public final void addTicketTag(long ticket, @NotNull String tag) throws DatabaseException {
        this.wrap(() -> this.addTicketTag0(ticket, tag));
    }

    protected abstract void addTicketTag0(long ticket, @NotNull String tag) throws SQLException;

    @Override
    public final void removeTicketTag(long ticket, @NotNull String tag) throws DatabaseException {
        this.wrap(() -> this.removeTicketTag0(ticket, tag));
    }

    protected abstract void removeTicketTag0(long ticket, @NotNull String tag) throws SQLException;

    @Override
    public final void addTicketUser(long ticket, long user) throws DatabaseException {
        this.wrap(() -> this.addTicketUser0(ticket, user));
    }

    protected abstract void addTicketUser0(long ticket, long user) throws SQLException;

    @Override
    public final void removeTicketUser(long ticket, long user) throws DatabaseException {
        this.wrap(() -> this.removeTicketUser0(ticket, user));
    }

    protected abstract void removeTicketUser0(long ticket, long user) throws SQLException;
}
