package org.burrow_studios.obelisk.server.moderation.db.ticket;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

class MySQLTicketDB extends SQLTicketDB {
    private static final String URL = "jdbc:mysql://{0}:{1}/{2}";

    private static final String CREATE_TABLE_TICKETS      = "CREATE TABLE IF NOT EXISTS `tickets` (`id` BIGINT(20) NOT NULL, `title` TEXT NULL, `state` TEXT NOT NULL, PRIMARY KEY (`id`));";
    private static final String CREATE_TABLE_TICKET_TAGS  = "CREATE TABLE IF NOT EXISTS `ticket_tags` (`ticket` BIGINT(20) NOT NULL, `tag` VARCHAR(256) NOT NULL, PRIMARY KEY (`ticket`, `tag`));";
    private static final String CREATE_TABLE_TICKET_USERS = "CREATE TABLE IF NOT EXISTS `ticket_users` (`ticket` BIGINT(20) NOT NULL, `user` BIGINT(20) NOT NULL, PRIMARY KEY (`ticket`, `user`));";
    private static final String ALTER_TABLE_TICKET_TAGS  = "ALTER TABLE `ticket_tags` ADD FOREIGN KEY (`ticket`) REFERENCES `tickets`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;";
    private static final String ALTER_TABLE_TICKET_USERS = "ALTER TABLE `ticket_users` ADD FOREIGN KEY (`ticket`) REFERENCES `tickets`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;";

    private static final String GET_TICKET_IDS = "SELECT `id` FROM `tickets`;";
    private static final String GET_TICKET     = "SELECT * FROM `tickets` WHERE `id` = ?;";
    private static final String GET_TICKET_TAGS  = "SELECT `tag` FROM `ticket_tags` WHERE `ticket` = ?;";
    private static final String GET_TICKET_USERS = "SELECT `user` FROM `ticket_users` WHERE `ticket` = ?;";

    private static final String CREATE_TICKET = "INSERT INTO `tickets` (`id`, `title`, `state`) VALUES ('?', '?', '?');";

    private static final String UPDATE_TICKET_TITLE = "UPDATE `tickets` SET `title` = '?' WHERE `id` = ?;";
    private static final String UPDATE_TICKET_STATE = "UPDATE `tickets` SET `state` = '?' WHERE `id` = ?;";

    private static final String ADD_TICKET_TAG  = "INSERT INTO `ticket_tags` (`ticket`, `tag`) VALUES ('?', '?');";
    private static final String ADD_TICKET_USER = "INSERT INTO `ticket_users` (`ticket`, `user`) VALUES ('?', '?');";

    private static final String REMOVE_TICKET_TAG  = "DELETE FROM `ticket_tags` WHERE `ticket` = ? AND `tag` = ?;";
    private static final String REMOVE_TICKET_USER = "DELETE FROM `ticket_users` WHERE `ticket` = ? AND `user` = ?;";

    private final Connection connection;

    public MySQLTicketDB(@NotNull String host, int port, @NotNull String database, @NotNull String user, @NotNull String pass) throws DatabaseException {
        final String url = MessageFormat.format(URL, host, port, database);

        try {
            this.connection = DriverManager.getConnection(url, user, pass);

            final PreparedStatement createTickets     = this.connection.prepareStatement(CREATE_TABLE_TICKETS);
            final PreparedStatement createTicketTags  = this.connection.prepareStatement(CREATE_TABLE_TICKET_TAGS);
            final PreparedStatement createTicketUsers = this.connection.prepareStatement(CREATE_TABLE_TICKET_USERS);

            final PreparedStatement alterTicketTags  = this.connection.prepareStatement(ALTER_TABLE_TICKET_TAGS);
            final PreparedStatement alterTicketUsers = this.connection.prepareStatement(ALTER_TABLE_TICKET_USERS);

            createTickets.execute();
            createTicketTags.execute();
            createTicketUsers.execute();

            alterTicketTags.execute();
            alterTicketUsers.execute();
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
