package org.burrow_studios.obelkisk.db;

import org.burrow_studios.obelisk.util.turtle.TurtleGenerator;
import org.burrow_studios.obelkisk.Main;
import org.burrow_studios.obelkisk.db.interfaces.DiscordAccountDB;
import org.burrow_studios.obelkisk.db.interfaces.TicketDB;
import org.burrow_studios.obelkisk.entity.DiscordAccount;
import org.burrow_studios.obelkisk.entity.Ticket;
import org.burrow_studios.obelkisk.exceptions.DatabaseException;
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

public class DatabaseImpl implements TicketDB, DiscordAccountDB, Closeable {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseImpl.class);

    private final SQLDB database;
    private final TurtleGenerator ids = TurtleGenerator.get("entities");

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

    /* - - - */

    @Override
    public @NotNull Ticket createTicket(@NotNull String title) throws DatabaseException {
        final long id = ids.newId();

        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_create")) {
            stmt.setLong(1, id);
            stmt.setString(2, title);

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
                long id = res.getLong("id");

                tickets.add(new Ticket(id, this));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Collections.unmodifiableList(tickets);
    }

    @Override
    public @NotNull String getTicketTitle(long id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_get_title")) {
            stmt.setLong(1, id);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                throw new DatabaseException("Ticket " + id + " does not exist");

            return res.getString("title");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void setTicketTitle(long id, @NotNull String title) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_title_set")) {
            stmt.setString(1, title);
            stmt.setLong(2, id);

            stmt.execute();
        } catch (SQLException e) {
            // TODO: check for error and maybe throw a NoSuchEntryException
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteTicket(long id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_delete")) {
            stmt.setLong(1, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public @NotNull DiscordAccount createDiscordAccount(long snowflake, @NotNull String name) throws DatabaseException {
        final long id = ids.newId();

        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_create")) {
            stmt.setLong(1, id);
            stmt.setLong(2, snowflake);
            stmt.setString(3, name);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return new DiscordAccount(id, this);
    }

    @Override
    public @NotNull List<DiscordAccount> listDiscordAccounts() throws DatabaseException {
        List<DiscordAccount> discordAccounts = new ArrayList<>();

        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_list")) {
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                long id = res.getLong("id");

                discordAccounts.add(new DiscordAccount(id, this));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Collections.unmodifiableList(discordAccounts);
    }

    @Override
    public long getDiscordAccountSnowflake(long id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_get_snowflake")) {
            stmt.setLong(1, id);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                throw new DatabaseException("DiscordAccount " + id + " does not exist");

            return res.getLong("snowflake");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public @NotNull String getDiscordAccountName(long id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_get_name")) {
            stmt.setLong(1, id);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                throw new DatabaseException("DiscordAccount " + id + " does not exist");

            return res.getString("name");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void setDiscordAccountName(long id, @NotNull String name) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_name_set")) {
            stmt.setString(1, name);
            stmt.setLong(2, id);

            stmt.execute();
        } catch (SQLException e) {
            // TODO: check for error and maybe throw a NoSuchEntryException
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteDiscordAccount(long id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_delete")) {
            stmt.setLong(1, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
