package org.burrow_studios.obelkisk.server.db.sql;

import org.burrow_studios.obelisk.api.entity.Ticket;
import org.burrow_studios.obelisk.api.entity.User;
import org.burrow_studios.obelisk.api.entity.dao.TicketDAO;
import org.burrow_studios.obelisk.api.entity.dao.UserDAO;
import org.burrow_studios.obelkisk.server.Main;
import org.burrow_studios.obelkisk.server.Obelisk;
import org.burrow_studios.obelkisk.server.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConfig;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseImpl implements TicketDAO, UserDAO, Closeable {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseImpl.class);

    private final Obelisk obelisk;
    private final SQLDB database;
    private final AtomicInteger  ticketIncrement = new AtomicInteger(0);

    public DatabaseImpl(@NotNull Obelisk obelisk, @NotNull File file) {
        this.obelisk = obelisk;

        String url = String.format("jdbc:sqlite:%s", file.getAbsolutePath());

        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);

        LOG.info("Initializing database connection to {}", url);
        try {
            this.database = new SQLDB(url, config.toProperties(), SQLDB.resourceFunction(Main.class, "/sql/entities/"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        LOG.info("Creating tables");
        try {
            this.database.execute("user/table");

            this.database.execute("ticket/table");
            this.database.execute("ticket/table_users");

            this.resetTicketIncrement();
        } catch (SQLException e) {
            throw new RuntimeException("Could not create tables", e);
        }

        LOG.info("Database is online");
    }

    @Override
    public void close() throws IOException {
        LOG.warn("Shutting down");
        this.database.close();
    }

    private void resetTicketIncrement() throws SQLException {
        try (ResultSet res = this.database.executeQuery("ticket/ticket_max_id")) {
            this.ticketIncrement.set(res.getInt("id") + 1);
        }
    }

    /* - - - */

    @Override
    public @NotNull Ticket createTicket(long channel) throws DatabaseException {
        final int id = this.ticketIncrement.getAndIncrement();

        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_create")) {
            stmt.setInt(1, id);
            stmt.setLong(2, channel);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return new Ticket(id, this);
    }

    @Override
    public @NotNull List<Ticket> listTickets() throws DatabaseException {
        List<Ticket> tickets = new ArrayList<>();

        try (PreparedStatement stmt = this.database.preparedStatement("ticket/tickets_list")) {
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                int id = res.getInt("id");

                tickets.add(new Ticket(id, this));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Collections.unmodifiableList(tickets);
    }

    @Override
    public @NotNull Optional<Ticket> getTicket(int id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_get")) {
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                return Optional.empty();

            return Optional.of(new Ticket(id, this));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public long getTicketChannel(int id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_channel_get")) {
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                throw new DatabaseException("Ticket " + id + " does not exist");

            return res.getLong("channel");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public @NotNull List<User> getTicketUsers(int id) throws DatabaseException {
        List<User> users = new ArrayList<>();

        UserDAO userDB = this.obelisk.getUserDAO();

        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_users_get")) {
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                long userId = res.getLong("user");

                userDB.getUser(userId)
                        .ifPresent(users::add);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Collections.unmodifiableList(users);
    }

    @Override
    public void addTicketUser(int id, @NotNull User user) {
        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_user_add")) {
            stmt.setInt(1, id);
            stmt.setLong(2, user.getSnowflake());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void removeTicketUser(int id, @NotNull User user) {
        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_user_remove")) {
            stmt.setInt(1, id);
            stmt.setLong(2, user.getSnowflake());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteTicket(int id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_delete")) {
            stmt.setInt(1, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public @NotNull User createUser(long snowflake, @NotNull String name) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("user/user_create")) {
            stmt.setLong(1, snowflake);
            stmt.setString(2, name);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return new User(snowflake, this);
    }

    @Override
    public @NotNull List<User> listUsers() throws DatabaseException {
        List<User> users = new ArrayList<>();

        try (PreparedStatement stmt = this.database.preparedStatement("user/users_list")) {
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                long snowflake = res.getLong("snowflake");

                users.add(new User(snowflake, this));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Collections.unmodifiableList(users);
    }

    @Override
    public @NotNull Optional<User> getUser(long snowflake) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("user/user_get")) {
            stmt.setLong(1, snowflake);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                return Optional.empty();

            return Optional.of(new User(snowflake, this));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public @NotNull String getUserName(long snowflake) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("user/user_name_get")) {
            stmt.setLong(1, snowflake);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                throw new DatabaseException("DiscordAccount " + snowflake + " does not exist");

            return res.getString("name");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void setUserName(long snowflake, @NotNull String name) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("user/user_name_set")) {
            stmt.setString(1, name);
            stmt.setLong(2, snowflake);

            stmt.execute();
        } catch (SQLException e) {
            // TODO: check for error and maybe throw a NoSuchEntryException
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteUser(long snowflake) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("user/user_delete")) {
            stmt.setLong(1, snowflake);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
