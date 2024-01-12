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

    private static final String GET_TICKET_IDS = getStatementFromResources("/sql/entities/ticket/get_tickets.sql");
    private static final String GET_TICKET     = getStatementFromResources("/sql/entities/ticket/get_ticket.sql");
    private static final String GET_TICKET_TAGS  = getStatementFromResources("/sql/entities/ticket/get_ticket_tags.sql");
    private static final String GET_TICKET_USERS = getStatementFromResources("/sql/entities/ticket/get_ticket_users.sql");

    private static final String CREATE_TICKET = getStatementFromResources("/sql/entities/ticket/create_ticket.sql");

    private static final String UPDATE_TICKET_TITLE = getStatementFromResources("/sql/entities/ticket/update_ticket_title.sql");
    private static final String UPDATE_TICKET_STATE = getStatementFromResources("/sql/entities/ticket/update_ticket_state.sql");

    private static final String ADD_TICKET_TAG  = getStatementFromResources("/sql/entities/ticket/add_ticket_tag.sql");
    private static final String ADD_TICKET_USER = getStatementFromResources("/sql/entities/ticket/add_ticket_user.sql");

    private static final String REMOVE_TICKET_TAG  = getStatementFromResources("/sql/entities/ticket/remove_ticket_tag.sql");
    private static final String REMOVE_TICKET_USER = getStatementFromResources("/sql/entities/ticket/remove_ticket_user.sql");

    private final Connection connection;

    public MySQLTicketDB(@NotNull String host, int port, @NotNull String database, @NotNull String user, @NotNull String pass) throws DatabaseException {
        final String url = URL.formatted(host, port, database);

        final String tableStmt = getStatementFromResources("/sql/entities/ticket/tables_ticket.sql");

        try {
            this.connection = DriverManager.getConnection(url, user, pass);

            this.connection.createStatement().execute(tableStmt);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    protected @NotNull Set<Long> getTicketIds0() throws SQLException {
        HashSet<Long> ids = new HashSet<>();

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_TICKET_IDS)) {
            ResultSet result = stmt.executeQuery();

            while (result.next())
                ids.add(result.getLong(1));
        }

        return Set.copyOf(ids);
    }

    @Override
    protected @NotNull JsonObject getTicket0(long id) throws SQLException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_TICKET)) {
            stmt.setLong(1, id);

            ResultSet result = stmt.executeQuery();

            if (!result.next())
                throw new NoSuchEntryException();

            json.addProperty("title", result.getString("title"));
            json.addProperty("state", result.getString("state"));
        }

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_TICKET_TAGS)) {
            stmt.setLong(1, id);

            JsonArray arr = new JsonArray();

            ResultSet result = stmt.executeQuery();

            while (result.next())
                arr.add(result.getString("tag"));

            json.add("tags", arr);
        }

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_TICKET_USERS)) {
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
        try (PreparedStatement stmt = this.connection.prepareStatement(CREATE_TICKET)) {
            stmt.setLong(1, id);
            stmt.setString(2, title);
            stmt.setString(3, state.name());

            stmt.execute();
        }
    }

    @Override
    protected void updateTicketTitle0(long id, @Nullable String title) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(UPDATE_TICKET_TITLE)) {
            stmt.setString(1, title);
            stmt.setLong(2, id);

            stmt.execute();
        }
    }

    @Override
    protected void updateTicketState0(long id, @NotNull TicketState state) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(UPDATE_TICKET_STATE)) {
            stmt.setString(1, state.name());
            stmt.setLong(2, id);

            stmt.execute();
        }
    }

    @Override
    protected void addTicketTag0(long ticket, @NotNull String tag) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(ADD_TICKET_TAG)) {
            stmt.setLong(1, ticket);
            stmt.setString(2, tag);

            stmt.execute();
        }
    }

    @Override
    protected void removeTicketTag0(long ticket, @NotNull String tag) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(REMOVE_TICKET_TAG)) {
            stmt.setLong(1, ticket);
            stmt.setString(2, tag);

            stmt.execute();
        }
    }

    @Override
    protected void addTicketUser0(long ticket, long user) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(ADD_TICKET_USER)) {
            stmt.setLong(1, ticket);
            stmt.setLong(2, user);

            stmt.execute();
        }
    }

    @Override
    protected void removeTicketUser0(long ticket, long user) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(REMOVE_TICKET_USER)) {
            stmt.setLong(1, ticket);
            stmt.setLong(2, user);

            stmt.execute();
        }
    }
}
