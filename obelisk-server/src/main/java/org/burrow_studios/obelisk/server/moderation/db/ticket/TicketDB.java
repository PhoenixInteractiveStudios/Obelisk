package org.burrow_studios.obelisk.server.moderation.db.ticket;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.moderation.ModerationService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface TicketDB {
    static @NotNull TicketDB get(@NotNull ModerationService modService) {
        final String host     = "null";
        final int    port     = 3306;
        final String database = "null";
        final String user     = "null";
        final String pass     = "null";

        return new MySQLTicketDB(host, port, database, user, pass);
    }

    @NotNull Set<Long> getTicketIds() throws DatabaseException;

    @NotNull JsonObject getTicket(long id) throws DatabaseException;

    void createTicket(long id, @Nullable String title, @NotNull TicketState state) throws DatabaseException;

    void updateTicketTitle(long id, @Nullable String title) throws DatabaseException;

    void updateTicketState(long id, @NotNull TicketState state) throws DatabaseException;

    void addTicketTag(long ticket, @NotNull String tag) throws DatabaseException;

    void removeTicketTag(long ticket, @NotNull String tag) throws DatabaseException;

    void addTicketUser(long ticket, long user) throws DatabaseException;

    void removeTicketUser(long ticket, long user) throws DatabaseException;
}
