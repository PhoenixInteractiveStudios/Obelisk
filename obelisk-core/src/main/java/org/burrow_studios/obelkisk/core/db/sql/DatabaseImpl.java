package org.burrow_studios.obelkisk.core.db.sql;

import org.burrow_studios.obelisk.util.turtle.TurtleGenerator;
import org.burrow_studios.obelkisk.core.Main;
import org.burrow_studios.obelkisk.core.db.interfaces.UserDB;
import org.burrow_studios.obelkisk.core.entity.User;
import org.burrow_studios.obelkisk.core.exceptions.DatabaseException;
import org.burrow_studios.obelkisk.core.db.interfaces.DiscordAccountDB;
import org.burrow_studios.obelkisk.core.db.interfaces.TicketDB;
import org.burrow_studios.obelkisk.core.entity.DiscordAccount;
import org.burrow_studios.obelkisk.core.entity.Ticket;
import org.burrow_studios.obelkisk.core.exceptions.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseImpl implements UserDB, TicketDB, DiscordAccountDB, Closeable {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseImpl.class);

    private final SQLDB database;
    private final TurtleGenerator userIds = TurtleGenerator.get("users");
    private final AtomicInteger ticketIncrement = new AtomicInteger(0);

    public DatabaseImpl(@NotNull File file) {
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
            this.database.execute("ticket/table");

            this.database.execute("discord/table");

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
    public @NotNull User createUser(@NotNull String name, @Nullable String pronouns) throws DatabaseException {
        final long id = this.userIds.newId();

        try (PreparedStatement stmt = this.database.preparedStatement("user/user_create")) {
            stmt.setLong(1, id);
            stmt.setString(2, name);
            stmt.setString(3, pronouns);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return new User(id, this);
    }

    @Override
    public @NotNull List<User> listUsers() throws DatabaseException {
        List<User> users = new ArrayList<>();

        try (PreparedStatement stmt = this.database.preparedStatement("user/users_list")) {
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                long id = res.getLong("id");

                users.add(new User(id, this));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Collections.unmodifiableList(users);
    }

    @Override
    public @NotNull User getUser(long id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("user/user_get")) {
            stmt.setLong(1, id);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                throw new NoSuchEntryException();

            return new User(id, this);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public @NotNull String getUserName(long id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("user/user_name_get")) {
            stmt.setLong(1, id);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                throw new DatabaseException("User " + id + " does not exist");

            return res.getString("name");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public @Nullable String getUserPronouns(long id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("user/user_pronouns_get")) {
            stmt.setLong(1, id);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                throw new DatabaseException("User " + id + " does not exist");

            return res.getString("name");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void setUserName(long id, @NotNull String name) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("user/user_name_set")) {
            stmt.setString(1, name);
            stmt.setLong(2, id);

            stmt.execute();
        } catch (SQLException e) {
            // TODO: check for error and maybe throw a NoSuchEntryException
            throw new DatabaseException(e);
        }
    }

    @Override
    public void setUserPronouns(long id, @Nullable String pronouns) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("user/user_pronouns_set")) {
            stmt.setString(1, pronouns);
            stmt.setLong(2, id);

            stmt.execute();
        } catch (SQLException e) {
            // TODO: check for error and maybe throw a NoSuchEntryException
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteUser(long id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("user/user_delete")) {
            stmt.setLong(1, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public @NotNull Ticket createTicket(long channel, @NotNull String title) throws DatabaseException {
        final int id = this.ticketIncrement.getAndIncrement();

        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_create")) {
            stmt.setInt(1, id);
            stmt.setLong(2, channel);
            stmt.setString(3, title);

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
    public @NotNull Ticket getTicket(int id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_get")) {
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                throw new NoSuchEntryException();

            return new Ticket(id, this);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public @NotNull String getTicketTitle(int id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_title_get")) {
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                throw new DatabaseException("Ticket " + id + " does not exist");

            return res.getString("title");
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
    public void setTicketTitle(int id, @NotNull String title) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_title_set")) {
            stmt.setString(1, title);
            stmt.setInt(2, id);

            stmt.execute();
        } catch (SQLException e) {
            // TODO: check for error and maybe throw a NoSuchEntryException
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
    public @NotNull DiscordAccount createDiscordAccount(long snowflake, @NotNull String name) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_create")) {
            stmt.setLong(1, snowflake);
            stmt.setString(2, name);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return new DiscordAccount(snowflake, this);
    }

    @Override
    public @NotNull List<DiscordAccount> listDiscordAccounts() throws DatabaseException {
        List<DiscordAccount> discordAccounts = new ArrayList<>();

        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_list")) {
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                long snowflake = res.getLong("snowflake");

                discordAccounts.add(new DiscordAccount(snowflake, this));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Collections.unmodifiableList(discordAccounts);
    }

    @Override
    public @NotNull DiscordAccount getDiscordAccount(long snowflake) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_get")) {
            stmt.setLong(1, snowflake);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                throw new NoSuchEntryException();

            return new DiscordAccount(snowflake, this);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public @NotNull String getDiscordAccountName(long snowflake) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_get_name")) {
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
    public void setDiscordAccountName(long snowflake, @NotNull String name) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_name_set")) {
            stmt.setString(1, name);
            stmt.setLong(2, snowflake);

            stmt.execute();
        } catch (SQLException e) {
            // TODO: check for error and maybe throw a NoSuchEntryException
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteDiscordAccount(long snowflake) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_delete")) {
            stmt.setLong(1, snowflake);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
