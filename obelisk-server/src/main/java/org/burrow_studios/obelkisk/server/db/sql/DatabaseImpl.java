package org.burrow_studios.obelkisk.server.db.sql;

import org.burrow_studios.obelisk.api.entity.*;
import org.burrow_studios.obelisk.api.entity.dao.*;
import org.burrow_studios.obelisk.util.turtle.TurtleGenerator;
import org.burrow_studios.obelkisk.server.Main;
import org.burrow_studios.obelkisk.server.Obelisk;
import org.burrow_studios.obelkisk.server.exceptions.DatabaseException;
import org.burrow_studios.obelkisk.server.exceptions.NoSuchEntryException;
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
import java.sql.Types;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseImpl implements UserDAO, TicketDAO, ProjectDAO, DiscordAccountDAO, MinecraftAccountDAO, Closeable {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseImpl.class);

    private final Obelisk obelisk;
    private final SQLDB database;
    private final TurtleGenerator userIds = TurtleGenerator.get("users");
    private final AtomicInteger  ticketIncrement = new AtomicInteger(0);
    private final AtomicInteger projectIncrement = new AtomicInteger(0);

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

            this.database.execute("project/table");
            this.database.execute("project/table_members");

            this.database.execute("discord/table");

            this.database.execute("minecraft/table");

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
    public @NotNull Optional<User> getUser(long id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("user/user_get")) {
            stmt.setLong(1, id);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                return Optional.empty();

            return Optional.of(new User(id, this));
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
            stmt.setLong(2, user.getId());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void removeTicketUser(int id, @NotNull User user) {
        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_user_remove")) {
            stmt.setInt(1, id);
            stmt.setLong(2, user.getId());

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
    public @NotNull Project createProject(@NotNull String title) throws DatabaseException {
        final int id = this.projectIncrement.getAndIncrement();

        try (PreparedStatement stmt = this.database.preparedStatement("project/project_create")) {
            stmt.setInt(1, id);
            stmt.setString(2, title);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return new Project(id, this);
    }

    @Override
    public @NotNull List<Project> listProjects() throws DatabaseException {
        List<Project> projects = new ArrayList<>();

        try (PreparedStatement stmt = this.database.preparedStatement("project/projects_list")) {
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                int id = res.getInt("id");

                projects.add(new Project(id, this));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Collections.unmodifiableList(projects);
    }

    @Override
    public @NotNull Optional<Project> getProject(int id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("project/project_get")) {
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                return Optional.empty();

            return Optional.of(new Project(id, this));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public @NotNull String getProjectTitle(int id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("project/project_title_get")) {
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                throw new DatabaseException("Project " + id + " does not exist");

            return res.getString("title");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public @Nullable String getProjectApplicationTemplate(int id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("project/project_application_template_get")) {
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                throw new DatabaseException("Project " + id + " does not exist");

            return res.getString("application_template");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean isProjectInviteOnly(int id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("project/project_invite_only_get")) {
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                throw new DatabaseException("Project " + id + " does not exist");

            return res.getBoolean("invite_only");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void setProjectTitle(int id, @NotNull String title) {
        try (PreparedStatement stmt = this.database.preparedStatement("project/project_title_set")) {
            stmt.setString(1, title);
            stmt.setInt(2, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public @NotNull List<User> getProjectMembers(int id) throws DatabaseException {
        List<User> users = new ArrayList<>();

        UserDAO userDB = this.obelisk.getUserDAO();

        try (PreparedStatement stmt = this.database.preparedStatement("project/project_members_get")) {
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
    public void addProjectMember(int id, @NotNull User user) {
        try (PreparedStatement stmt = this.database.preparedStatement("project/project_member_add")) {
            stmt.setInt(1, id);
            stmt.setLong(2, user.getId());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void removeProjectMember(int id, @NotNull User user) {
        try (PreparedStatement stmt = this.database.preparedStatement("project/project_member_remove")) {
            stmt.setInt(1, id);
            stmt.setLong(2, user.getId());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteProject(int id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("project/project_delete")) {
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
    public @NotNull Optional<DiscordAccount> getDiscordAccount(long snowflake) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_get")) {
            stmt.setLong(1, snowflake);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                return Optional.empty();

            return Optional.of(new DiscordAccount(snowflake, this));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public @Nullable User getDiscordAccountUser(long snowflake) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_user_get")) {
            stmt.setLong(1, snowflake);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                return null;

            long userId = res.getLong("user");
            return this.obelisk.getUserDAO().getUser(userId)
                    .orElseThrow(NoSuchEntryException::new);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public @NotNull String getDiscordAccountName(long snowflake) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_name_get")) {
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
    public void setDiscordAccountUser(long snowflake, @Nullable User user) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_user_set")) {
            if (user == null)
                stmt.setNull(1, Types.BIGINT);
            else
                stmt.setLong(1, user.getId());

            stmt.setLong(2, snowflake);

            stmt.execute();
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

    @Override
    public @NotNull MinecraftAccount createMinecraftAccount(@NotNull UUID uuid, @NotNull String name) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("minecraft/minecraft_create")) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, name);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return new MinecraftAccount(uuid, this);
    }

    @Override
    public @NotNull List<MinecraftAccount> listMinecraftAccounts() throws DatabaseException {
        List<MinecraftAccount> minecraftAccounts = new ArrayList<>();

        try (PreparedStatement stmt = this.database.preparedStatement("minecraft/minecraft_list")) {
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                String uuidStr = res.getString("uuid");
                UUID uuid = UUID.fromString(uuidStr);

                minecraftAccounts.add(new MinecraftAccount(uuid, this));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Collections.unmodifiableList(minecraftAccounts);
    }

    @Override
    public @NotNull Optional<MinecraftAccount> getMinecraftAccount(@NotNull UUID uuid) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("minecraft/minecraft_get")) {
            stmt.setString(1, uuid.toString());

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                return Optional.empty();

            return Optional.of(new MinecraftAccount(uuid, this));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public @Nullable User getMinecraftAccountUser(@NotNull UUID uuid) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("minecraft/minecraft_user_get")) {
            stmt.setString(1, uuid.toString());

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                return null;

            long userId = res.getLong("user");
            return this.obelisk.getUserDAO().getUser(userId)
                    .orElseThrow(NoSuchEntryException::new);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public @NotNull String getMinecraftAccountName(@NotNull UUID uuid) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("minecraft/minecraft_name_get")) {
            stmt.setString(1, uuid.toString());

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                throw new DatabaseException("MinecraftAccount " + uuid + " does not exist");

            return res.getString("name");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void setMinecraftAccountUser(@NotNull UUID uuid, @Nullable User user) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("minecraft/minecraft_user_set")) {
            if (user == null)
                stmt.setNull(1, Types.BIGINT);
            else
                stmt.setLong(1, user.getId());

            stmt.setString(2, uuid.toString());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void setMinecraftAccountName(@NotNull UUID uuid, @NotNull String name) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("minecraft/minecraft_name_set")) {
            stmt.setString(1, name);
            stmt.setString(2, uuid.toString());

            stmt.execute();
        } catch (SQLException e) {
            // TODO: check for error and maybe throw a NoSuchEntryException
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteMinecraftAccount(@NotNull UUID uuid) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("minecraft/minecraft_delete")) {
            stmt.setString(1, uuid.toString());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
