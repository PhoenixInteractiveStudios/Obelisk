package org.burrow_studios.obelisk.monolith.db.impl;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.cache.OrderedEntitySetView;
import org.burrow_studios.obelisk.core.entities.AbstractDiscordAccount;
import org.burrow_studios.obelisk.core.entities.AbstractMinecraftAccount;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.monolith.Main;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.db.SQLDB;
import org.burrow_studios.obelisk.monolith.entities.*;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.burrow_studios.obelisk.monolith.exceptions.NoSuchEntryException;
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
import java.util.List;
import java.util.UUID;

public class EntityDatabase implements Closeable {
    private static final Logger LOG = LoggerFactory.getLogger(EntityDatabase.class);

    private final ObeliskMonolith obelisk;
    private final SQLDB database;

    public EntityDatabase(@NotNull ObeliskMonolith obelisk, @NotNull File file) throws DatabaseException {
        this.obelisk = obelisk;

        String url = String.format("jdbc:sqlite:%s", file.getAbsolutePath());

        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);

        LOG.info("Initializing database connection to {}", url);
        try {
            database = new SQLDB(url, config.toProperties(), SQLDB.resourceFunction(Main.class, "/sql/entities/"));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        LOG.info("Creating tables");
        try {
            this.database.execute("user/table");

            this.database.execute("discord/table");
            this.database.execute("discord/table_users");

            this.database.execute("minecraft/table");
            this.database.execute("minecraft/table_users");

            this.database.execute("ticket/table");
            this.database.execute("ticket/table_users");

            this.database.execute("project/table");
            this.database.execute("project/table_members");
        } catch (SQLException e) {
            throw new DatabaseException("Could not create tables", e);
        }

        LOG.info("Database is online");
    }

    @Override
    public void close() throws IOException {
        LOG.warn("Shutting down");
        this.database.close();
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - */

    public List<BackendUser> getUsers() throws DatabaseException {
        List<BackendUser> users = new ArrayList<>();

        try (PreparedStatement stmt0 = this.database.preparedStatement("user/users_list")) {
            ResultSet res = stmt0.executeQuery();

            while (res.next()) {
                final long id = res.getLong("id");

                try (
                        PreparedStatement stmt1 = this.database.preparedStatement("user/discord_get");
                        PreparedStatement stmt2 = this.database.preparedStatement("user/minecraft_get")
                ) {
                    stmt1.setLong(1, id);
                    stmt2.setLong(1, id);

                    ResultSet dRes = stmt1.executeQuery();
                    ResultSet mRes = stmt2.executeQuery();

                    BackendUser user = EntityProvider.getUser(obelisk, id, res, dRes, mRes);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return users;
    }

    public BackendUser getUser(long id) throws DatabaseException {
        try (
                PreparedStatement stmt0 = this.database.preparedStatement("user/user_get");
                PreparedStatement stmt1 = this.database.preparedStatement("user/discord_get");
                PreparedStatement stmt2 = this.database.preparedStatement("user/minecraft_get");
        ) {
            stmt0.setLong(1, id);
            stmt1.setLong(1, id);
            stmt2.setLong(1, id);

            ResultSet  res = stmt0.executeQuery();
            ResultSet dRes = stmt1.executeQuery();
            ResultSet mRes = stmt2.executeQuery();

            if (!res.next())
                throw new NoSuchEntryException();

            return EntityProvider.getUser(obelisk, id, res, dRes, mRes);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public BackendUser createUser(long id, @NotNull String name) throws DatabaseException {
        OrderedEntitySetView<AbstractDiscordAccount>   discord   = new OrderedEntitySetView<>(obelisk.getDiscordAccounts(),   AbstractDiscordAccount.class);
        OrderedEntitySetView<AbstractMinecraftAccount> minecraft = new OrderedEntitySetView<>(obelisk.getMinecraftAccounts(), AbstractMinecraftAccount.class);

        try (PreparedStatement stmt = this.database.preparedStatement("user/user_create")) {
            stmt.setLong(1, id);
            stmt.setString(2, name);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return new BackendUser(obelisk, id, name, discord, minecraft);
    }

    public void modifyUserName(long id, @NotNull String name) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("user/user_update_name")) {
            stmt.setString(1, name);

            stmt.setLong(2, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public void deleteUser(long id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("user/user_delete")) {
            stmt.setLong(1, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public List<BackendTicket> getTickets() throws DatabaseException {
        List<BackendTicket> tickets = new ArrayList<>();

        try (PreparedStatement stmt0 = this.database.preparedStatement("ticket/tickets_list")) {
            ResultSet res = stmt0.executeQuery();

            while (res.next()) {
                final long id = res.getLong("id");

                try (PreparedStatement stmt1 = this.database.preparedStatement("ticket/users_get")) {
                    stmt1.setLong(1, id);

                    ResultSet uRes = stmt1.executeQuery();

                    BackendTicket user = EntityProvider.getTicket(obelisk, id, res, uRes);
                    tickets.add(user);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return tickets;
    }

    public BackendTicket getTicket(long id) throws DatabaseException {
        try (
                PreparedStatement stmt0 = this.database.preparedStatement("ticket/ticket_get");
                PreparedStatement stmt1 = this.database.preparedStatement("ticket/users_get");
        ) {
            stmt0.setLong(1, id);
            stmt1.setLong(1, id);

            ResultSet  res = stmt0.executeQuery();
            ResultSet uRes = stmt1.executeQuery();

            if (!res.next())
                throw new NoSuchEntryException();

            return EntityProvider.getTicket(obelisk, id, res, uRes);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public BackendTicket createTicket(long id, @Nullable String title, @NotNull Ticket.State state) throws DatabaseException {
        OrderedEntitySetView<AbstractUser> users = new OrderedEntitySetView<>(obelisk.getUsers(), AbstractUser.class);

        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_create")) {
            stmt.setLong(1, id);
            stmt.setString(2, title);
            stmt.setString(3, state.name());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return new BackendTicket(obelisk, id, title, state, users);
    }

    public void modifyTicketTitle(long id, @Nullable String title) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_update_title")) {
            stmt.setString(1, title);

            stmt.setLong(2, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public void modifyTicketState(long id, @NotNull Ticket.State state) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_update_state")) {
            stmt.setString(1, state.name());

            stmt.setLong(2, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public void deleteTicket(long id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_delete")) {
            stmt.setLong(1, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public void addTicketUser(long ticket, long user) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_users_add")) {
            stmt.setLong(1, ticket);
            stmt.setLong(2, user);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public void removeTicketUser(long ticket, long user) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_users_remove")) {
            stmt.setLong(1, ticket);
            stmt.setLong(2, user);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public List<BackendProject> getProjects() throws DatabaseException {
        List<BackendProject> projects = new ArrayList<>();

        try (PreparedStatement stmt0 = this.database.preparedStatement("projects/projects_list")) {
            ResultSet res = stmt0.executeQuery();

            while (res.next()) {
                final long id = res.getLong("id");

                try (PreparedStatement stmt1 = this.database.preparedStatement("project/members_get")) {
                    stmt1.setLong(1, id);

                    ResultSet mRes = stmt1.executeQuery();

                    BackendProject user = EntityProvider.getProject(obelisk, id, res, mRes);
                    projects.add(user);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return projects;
    }

    public BackendProject getProject(long id) throws DatabaseException {
        try (
                PreparedStatement stmt0 = this.database.preparedStatement("project/project_get");
                PreparedStatement stmt1 = this.database.preparedStatement("project/members_get");
        ) {
            stmt0.setLong(1, id);
            stmt1.setLong(1, id);

            ResultSet  res = stmt0.executeQuery();
            ResultSet mRes = stmt1.executeQuery();

            if (!res.next())
                throw new NoSuchEntryException();

            return EntityProvider.getProject(obelisk, id, res, mRes);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public BackendProject createProject(long id, @NotNull String title, @NotNull Project.State state) throws DatabaseException {
        OrderedEntitySetView<AbstractUser> members = new OrderedEntitySetView<>(obelisk.getUsers(), AbstractUser.class);

        try (PreparedStatement stmt = this.database.preparedStatement("project/project_create")) {
            stmt.setLong(1, id);
            stmt.setString(2, title);
            stmt.setString(3, state.name());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return new BackendProject(obelisk, id, title, state, members);
    }

    public void modifyProjectTitle(long id, @NotNull String title) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("project/project_update_title")) {
            stmt.setString(1, title);

            stmt.setLong(2, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public void modifyProjectState(long id, @NotNull Project.State state) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("project/project_update_state")) {
            stmt.setString(1, state.name());

            stmt.setLong(2, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public void deleteProject(long id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("project/project_delete")) {
            stmt.setLong(1, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public void addProjectMember(long project, long user) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("project/project_members_add")) {
            stmt.setLong(1, project);
            stmt.setLong(2, user);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public void removeProjectMember(long project, long user) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("project/project_members_remove")) {
            stmt.setLong(1, project);
            stmt.setLong(2, user);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public List<BackendDiscordAccount> getDiscordAccounts() throws DatabaseException {
        List<BackendDiscordAccount> discordAccounts = new ArrayList<>();

        try (PreparedStatement stmt0 = this.database.preparedStatement("discord/discord_list")) {
            ResultSet res = stmt0.executeQuery();

            while (res.next()) {
                final long id = res.getLong("id");

                try (PreparedStatement stmt1 = this.database.preparedStatement("discord/user_get")) {
                    stmt1.setLong(1, id);

                    ResultSet uRes = stmt1.executeQuery();

                    BackendDiscordAccount user = EntityProvider.getDiscordAccount(obelisk, id, res, uRes);
                    discordAccounts.add(user);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return discordAccounts;
    }

    public BackendDiscordAccount getDiscordAccount(long id) throws DatabaseException {
        try (
                PreparedStatement stmt0 = this.database.preparedStatement("discord/discord_get");
                PreparedStatement stmt1 = this.database.preparedStatement("discord/user_get");
        ) {
            stmt0.setLong(1, id);
            stmt1.setLong(1, id);

            ResultSet  res = stmt0.executeQuery();
            ResultSet uRes = stmt1.executeQuery();

            if (!res.next())
                throw new NoSuchEntryException();

            return EntityProvider.getDiscordAccount(obelisk, id, res, uRes);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public BackendDiscordAccount createDiscordAccount(long id, long snowflake, @NotNull String name, @Nullable AbstractUser user) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_create")) {
            stmt.setLong(1, id);
            stmt.setLong(2, snowflake);
            stmt.setString(3, name);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        if (user != null) {
            // TODO: create link
        }

        return new BackendDiscordAccount(obelisk, id, snowflake, name, user);
    }

    public void modifyDiscordAccountName(long id, @NotNull String name) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_update_name")) {
            stmt.setString(1, name);

            stmt.setLong(2, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public void modifyDiscordAccountUser(long id, @Nullable User user) throws DatabaseException {
        // TODO: create link
    }

    public void deleteDiscordAccount(long id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_delete")) {
            stmt.setLong(1, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public List<BackendMinecraftAccount> getMinecraftAccounts() throws DatabaseException {
        List<BackendMinecraftAccount> minecraftAccounts = new ArrayList<>();

        try (PreparedStatement stmt0 = this.database.preparedStatement("minecraft/minecraft_list")) {
            ResultSet res = stmt0.executeQuery();

            while (res.next()) {
                final long id = res.getLong("id");

                try (PreparedStatement stmt1 = this.database.preparedStatement("minecraft/user_get")) {
                    stmt1.setLong(1, id);

                    ResultSet uRes = stmt1.executeQuery();

                    BackendMinecraftAccount user = EntityProvider.getMinecraftAccount(obelisk, id, res, uRes);
                    minecraftAccounts.add(user);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return minecraftAccounts;
    }

    public BackendMinecraftAccount getMinecraftAccount(long id) throws DatabaseException {
        try (
                PreparedStatement stmt0 = this.database.preparedStatement("minecraft/minecraft_get");
                PreparedStatement stmt1 = this.database.preparedStatement("minecraft/user_get");
        ) {
            stmt0.setLong(1, id);
            stmt1.setLong(1, id);

            ResultSet  res = stmt0.executeQuery();
            ResultSet uRes = stmt1.executeQuery();

            if (!res.next())
                throw new NoSuchEntryException();

            return EntityProvider.getMinecraftAccount(obelisk, id, res, uRes);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public BackendMinecraftAccount createMinecraftAccount(long id, @NotNull UUID uuid, @NotNull String name, @Nullable AbstractUser user) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("minecraft/minecraft_create")) {
            stmt.setLong(1, id);
            stmt.setString(2, uuid.toString());
            stmt.setString(3, name);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        if (user != null) {
            // TODO: create link
        }

        return new BackendMinecraftAccount(obelisk, id, uuid, name, user);
    }

    public void modifyMinecraftAccountName(long id, @NotNull String name) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("minecraft/minecraft_update_name")) {
            stmt.setString(1, name);

            stmt.setLong(2, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public void modifyMinecraftAccountUser(long id, @Nullable User user) throws DatabaseException {
        // TODO: create link
    }

    public void deleteMinecraftAccount(long id) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("minecraft/minecraft_delete")) {
            stmt.setLong(1, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
