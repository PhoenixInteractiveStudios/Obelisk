package org.burrow_studios.obelisk.server.moderation.db.ticket;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

class MySQLTicketDB extends SQLTicketDB {
    private static final String URL = "jdbc:mysql://%s:%s/%s?allowMultiQueries=true";

    public MySQLTicketDB(@NotNull String host, int port, @NotNull String database, @NotNull String user, @NotNull String pass) throws DatabaseException {
        super(URL.formatted(host, port, database), user, pass, "ticket/tables_ticket");
    }

    @Override
    protected @NotNull Set<Long> getTicketIds0() throws SQLException {
        HashSet<Long> ids = new HashSet<>();

        try (ResultSet result = executeQuery("ticket/get_tickets")) {
            while (result.next())
                ids.add(result.getLong(1));
        }

        return Set.copyOf(ids);
    }

    @Override
    protected @NotNull JsonObject getTicket0(long id) throws SQLException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        try (PreparedStatement stmt = prepareStatement("ticket/get_ticket")) {
            stmt.setLong(1, id);

            ResultSet result = stmt.executeQuery();

            if (!result.next())
                throw new NoSuchEntryException();

            json.addProperty("title", result.getString("title"));
            json.addProperty("state", result.getString("state"));
        }

        try (PreparedStatement stmt = prepareStatement("ticket/get_ticket_tags")) {
            stmt.setLong(1, id);

            JsonArray arr = new JsonArray();

            ResultSet result = stmt.executeQuery();

            while (result.next())
                arr.add(result.getString("tag"));

            json.add("tags", arr);
        }

        try (PreparedStatement stmt = prepareStatement("ticket/get_ticket_users")) {
            stmt.setLong(1, id);

            JsonArray arr = new JsonArray();

            ResultSet result = stmt.executeQuery();

            while (result.next())
                arr.add(result.getLong("user"));

            json.add("users", arr);
        }

        return json;
    }

    @Override
    protected void createTicket0(long id, @Nullable String title, @NotNull TicketState state) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("ticket/create_ticket")) {
            stmt.setLong(1, id);
            stmt.setString(2, title);
            stmt.setString(3, state.name());

            stmt.execute();
        }
    }

    @Override
    protected void updateTicketTitle0(long id, @Nullable String title) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("ticket/update_ticket_title")) {
            stmt.setString(1, title);
            stmt.setLong(2, id);

            stmt.execute();
        }
    }

    @Override
    protected void updateTicketState0(long id, @NotNull TicketState state) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("ticket/update_ticket_state")) {
            stmt.setString(1, state.name());
            stmt.setLong(2, id);

            stmt.execute();
        }
    }

    @Override
    protected void addTicketTag0(long ticket, @NotNull String tag) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("ticket/add_ticket_tag")) {
            stmt.setLong(1, ticket);
            stmt.setString(2, tag);

            stmt.execute();
        }
    }

    @Override
    protected void removeTicketTag0(long ticket, @NotNull String tag) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("ticket/remove_ticket_tag")) {
            stmt.setLong(1, ticket);
            stmt.setString(2, tag);

            stmt.execute();
        }
    }

    @Override
    protected void addTicketUser0(long ticket, long user) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("ticket/add_ticket_user")) {
            stmt.setLong(1, ticket);
            stmt.setLong(2, user);

            stmt.execute();
        }
    }

    @Override
    protected void removeTicketUser0(long ticket, long user) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("ticket/remove_ticket_user")) {
            stmt.setLong(1, ticket);
            stmt.setLong(2, user);

            stmt.execute();
        }
    }
}
