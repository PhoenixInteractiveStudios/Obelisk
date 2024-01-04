package org.burrow_studios.obelisk.server.moderation.db.ticket;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.Set;

class SQLiteTicketDB extends SQLTicketDB {
    @Override
    protected @NotNull Set<Long> getTicketIds0() throws SQLException {
        // TODO
        return null;
    }

    @Override
    protected @NotNull JsonObject getTicket0(long id) throws SQLException {
        // TODO
        return null;
    }

    @Override
    protected void createTicket0(long id, @Nullable String title, @NotNull TicketState state) throws SQLException {
        // TODO
    }

    @Override
    protected void updateTicketTitle0(long id, @Nullable String title) throws SQLException {
        // TODO
    }

    @Override
    protected void updateTicketState0(long id, @NotNull TicketState state) throws SQLException {
        // TODO
    }

    @Override
    protected void addTicketTag0(long ticket, @NotNull String tag) throws SQLException {
        // TODO
    }

    @Override
    protected void removeTicketTag0(long ticket, @NotNull String tag) throws SQLException {
        // TODO
    }

    @Override
    protected void addTicketUser0(long ticket, long user) throws SQLException {
        // TODO
    }

    @Override
    protected void removeTicketUser0(long ticket, long user) throws SQLException {
        // TODO
    }
}
