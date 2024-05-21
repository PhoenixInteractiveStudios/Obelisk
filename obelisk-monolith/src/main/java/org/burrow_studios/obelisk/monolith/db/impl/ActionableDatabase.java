package org.burrow_studios.obelisk.monolith.db.impl;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.cache.OrderedEntitySetView;
import org.burrow_studios.obelisk.core.entities.AbstractDiscordAccount;
import org.burrow_studios.obelisk.core.entities.AbstractMinecraftAccount;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.monolith.Main;
import org.burrow_studios.obelisk.monolith.action.entity.discord.DatabaseDiscordAccountBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.discord.DatabaseDiscordAccountDeleteAction;
import org.burrow_studios.obelisk.monolith.action.entity.discord.DatabaseDiscordAccountGetAction;
import org.burrow_studios.obelisk.monolith.action.entity.discord.DatabaseDiscordAccountModifier;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.DatabaseMinecraftAccountBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.DatabaseMinecraftAccountDeleteAction;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.DatabaseMinecraftAccountGetAction;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.DatabaseMinecraftAccountModifier;
import org.burrow_studios.obelisk.monolith.action.entity.project.*;
import org.burrow_studios.obelisk.monolith.action.entity.ticket.*;
import org.burrow_studios.obelisk.monolith.action.entity.user.DatabaseUserBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.user.DatabaseUserDeleteAction;
import org.burrow_studios.obelisk.monolith.action.entity.user.DatabaseUserGetAction;
import org.burrow_studios.obelisk.monolith.action.entity.user.DatabaseUserModifier;
import org.burrow_studios.obelisk.monolith.db.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.db.SQLDB;
import org.burrow_studios.obelisk.monolith.entities.*;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.burrow_studios.obelisk.monolith.exceptions.NoSuchEntryException;
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
import java.util.Objects;

public class ActionableDatabase implements IActionableDatabase, Closeable {
    private static final Logger LOG = LoggerFactory.getLogger(ActionableDatabase.class);

    private final SQLDB database;

    public ActionableDatabase(@NotNull File file) throws DatabaseException {
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
            this.database.execute("users/table");

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

    @Override
    public BackendUser onUserGet(@NotNull DatabaseUserGetAction action) throws DatabaseException {
        try (
                PreparedStatement stmt0 = this.database.preparedStatement("user/user_get");
                PreparedStatement stmt1 = this.database.preparedStatement("user/discord_get");
                PreparedStatement stmt2 = this.database.preparedStatement("user/minecraft_get");
        ) {
            stmt0.setLong(1, action.getId());
            stmt1.setLong(1, action.getId());
            stmt2.setLong(1, action.getId());

            ResultSet  res = stmt0.executeQuery();
            ResultSet dRes = stmt1.executeQuery();
            ResultSet mRes = stmt2.executeQuery();

            if (!res.next())
                throw new NoSuchEntryException();

            return EntityProvider.getUser(action.getAPI(), action.getId(), res, dRes, mRes);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public BackendUser onUserBuild(@NotNull DatabaseUserBuilder builder) throws DatabaseException {
        long id = 0; // TODO: generate id

        OrderedEntitySetView<AbstractDiscordAccount>   discord   = new OrderedEntitySetView<>(builder.getAPI().getDiscordAccounts(),   AbstractDiscordAccount.class);
        OrderedEntitySetView<AbstractMinecraftAccount> minecraft = new OrderedEntitySetView<>(builder.getAPI().getMinecraftAccounts(), AbstractMinecraftAccount.class);

        try (PreparedStatement stmt = this.database.preparedStatement("user/user_create")) {
            stmt.setLong(1, id);
            stmt.setString(2, builder.getName());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return new BackendUser(builder.getAPI(), id, builder.getName(), discord, minecraft);
    }

    @Override
    public void onUserModify(@NotNull DatabaseUserModifier modifier) throws DatabaseException {
        String oldName = modifier.getEntity().getName();
        String newName = modifier.getName();

        if (!Objects.equals(oldName, newName)) {
            try (PreparedStatement stmt = this.database.preparedStatement("user/user_update_name")) {
                stmt.setString(1, newName);

                stmt.setLong(2, modifier.getEntity().getId());

                stmt.execute();
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }
    }

    @Override
    public void onUserDelete(@NotNull DatabaseUserDeleteAction deleteAction) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("user/user_delete")) {
            stmt.setLong(1, deleteAction.getId());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public BackendTicket onTicketGet(@NotNull DatabaseTicketGetAction action) throws DatabaseException {
        try (
                PreparedStatement stmt0 = this.database.preparedStatement("ticket/ticket_get");
                PreparedStatement stmt1 = this.database.preparedStatement("ticket/users_get");
        ) {
            stmt0.setLong(1, action.getId());
            stmt1.setLong(1, action.getId());

            ResultSet  res = stmt0.executeQuery();
            ResultSet uRes = stmt1.executeQuery();

            if (!res.next())
                throw new NoSuchEntryException();

            return EntityProvider.getTicket(action.getAPI(), action.getId(), res, uRes);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public BackendTicket onTicketBuild(@NotNull DatabaseTicketBuilder builder) throws DatabaseException {
        long id = 0; // TODO: generate id

        OrderedEntitySetView<AbstractUser> users = new OrderedEntitySetView<>(builder.getAPI().getUsers(), AbstractUser.class);

        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_create")) {
            stmt.setLong(1, id);
            stmt.setString(2, builder.getTitle());
            stmt.setString(3, builder.getState().name());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return new BackendTicket(builder.getAPI(), id, builder.getTitle(), builder.getState(), users);
    }

    @Override
    public void onTicketModify(@NotNull DatabaseTicketModifier modifier) throws DatabaseException {
        String oldTitle = modifier.getEntity().getTitle();
        String newTitle = modifier.getTitle();

        if (!Objects.equals(oldTitle, newTitle)) {
            try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_update_title")) {
                stmt.setString(1, newTitle);

                stmt.setLong(2, modifier.getEntity().getId());

                stmt.execute();
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }

        Ticket.State oldState = modifier.getEntity().getState();
        Ticket.State newState = modifier.getState();

        if (!Objects.equals(oldState, newState)) {
            try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_update_state")) {
                stmt.setString(1, newState.name());

                stmt.setLong(2, modifier.getEntity().getId());

                stmt.execute();
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }
    }

    @Override
    public void onTicketDelete(@NotNull DatabaseTicketDeleteAction deleteAction) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_delete")) {
            stmt.setLong(1, deleteAction.getId());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void onTicketUserAdd(@NotNull DatabaseTicketUserAddAction action) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_users_add")) {
            stmt.setLong(1, action.getTicket().getId());
            stmt.setLong(2, action.getUser().getId());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void onTicketUserRemove(@NotNull DatabaseTicketUserRemoveAction action) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("ticket/ticket_users_remove")) {
            stmt.setLong(1, action.getTicket().getId());
            stmt.setLong(2, action.getUser().getId());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public BackendProject onProjectGet(@NotNull DatabaseProjectGetAction action) throws DatabaseException {
        try (
                PreparedStatement stmt0 = this.database.preparedStatement("project/project_get");
                PreparedStatement stmt1 = this.database.preparedStatement("project/users_get");
        ) {
            stmt0.setLong(1, action.getId());
            stmt1.setLong(1, action.getId());

            ResultSet  res = stmt0.executeQuery();
            ResultSet mRes = stmt1.executeQuery();

            if (!res.next())
                throw new NoSuchEntryException();

            return EntityProvider.getProject(action.getAPI(), action.getId(), res, mRes);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public BackendProject onProjectBuild(@NotNull DatabaseProjectBuilder builder) throws DatabaseException {
        long id = 0; // TODO: generate id

        OrderedEntitySetView<AbstractUser> members = new OrderedEntitySetView<>(builder.getAPI().getUsers(), AbstractUser.class);

        try (PreparedStatement stmt = this.database.preparedStatement("project/project_create")) {
            stmt.setLong(1, id);
            stmt.setString(2, builder.getTitle());
            stmt.setString(3, builder.getState().name());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return new BackendProject(builder.getAPI(), id, builder.getTitle(), builder.getState(), members);
    }

    @Override
    public void onProjectModify(@NotNull DatabaseProjectModifier modifier) throws DatabaseException {
        String oldTitle = modifier.getEntity().getTitle();
        String newTitle = modifier.getTitle();

        if (!Objects.equals(oldTitle, newTitle)) {
            try (PreparedStatement stmt = this.database.preparedStatement("project/project_update_title")) {
                stmt.setString(1, newTitle);

                stmt.setLong(2, modifier.getEntity().getId());

                stmt.execute();
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }

        Project.State oldState = modifier.getEntity().getState();
        Project.State newState = modifier.getState();

        if (!Objects.equals(oldState, newState)) {
            try (PreparedStatement stmt = this.database.preparedStatement("project/project_update_state")) {
                stmt.setString(1, newState.name());

                stmt.setLong(2, modifier.getEntity().getId());

                stmt.execute();
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }
    }

    @Override
    public void onProjectDelete(@NotNull DatabaseProjectDeleteAction deleteAction) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("project/project_delete")) {
            stmt.setLong(1, deleteAction.getId());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void onProjectUserAdd(@NotNull DatabaseProjectUserAddAction action) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("project/project_members_add")) {
            stmt.setLong(1, action.getProject().getId());
            stmt.setLong(2, action.getUser().getId());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void onProjectUserRemove(@NotNull DatabaseProjectUserRemoveAction action) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("project/project_members_remove")) {
            stmt.setLong(1, action.getProject().getId());
            stmt.setLong(2, action.getUser().getId());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public BackendDiscordAccount onDiscordAccountGet(@NotNull DatabaseDiscordAccountGetAction action) throws DatabaseException {
        try (
                PreparedStatement stmt0 = this.database.preparedStatement("discord/discord_get");
                PreparedStatement stmt1 = this.database.preparedStatement("discord/user_get");
        ) {
            stmt0.setLong(1, action.getId());
            stmt1.setLong(1, action.getId());

            ResultSet  res = stmt0.executeQuery();
            ResultSet uRes = stmt1.executeQuery();

            if (!res.next())
                throw new NoSuchEntryException();

            return EntityProvider.getDiscordAccount(action.getAPI(), action.getId(), res, uRes);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public BackendDiscordAccount onDiscordAccountBuild(@NotNull DatabaseDiscordAccountBuilder builder) throws DatabaseException {
        long id = 0; // TODO: generate id

        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_create")) {
            stmt.setLong(1, id);
            stmt.setLong(2, builder.getSnowflake());
            stmt.setString(3, builder.getName());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        if (builder.getUser() != null) {
            // TODO: create link
        }

        return new BackendDiscordAccount(builder.getAPI(), id, builder.getSnowflake(), builder.getName(), ((AbstractUser) builder.getUser()));
    }

    @Override
    public void onDiscordAccountModify(@NotNull DatabaseDiscordAccountModifier modifier) throws DatabaseException {
        String oldName = modifier.getEntity().getCachedName();
        String newName = modifier.getName();

        if (!Objects.equals(oldName, newName)) {
            try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_update_name")) {
                stmt.setString(1, newName);

                stmt.setLong(2, modifier.getEntity().getId());

                stmt.execute();
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }

        User oldUser = modifier.getEntity().getUser();
        User newUser = modifier.getUser();

        if (!Objects.equals(oldUser, newUser)) {
            // TODO: create link
        }
    }

    @Override
    public void onDiscordAccountDelete(@NotNull DatabaseDiscordAccountDeleteAction deleteAction) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("discord/discord_delete")) {
            stmt.setLong(1, deleteAction.getId());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public BackendMinecraftAccount onMinecraftAccountGet(@NotNull DatabaseMinecraftAccountGetAction action) throws DatabaseException {
        try (
                PreparedStatement stmt0 = this.database.preparedStatement("minecraft/minecraft_get");
                PreparedStatement stmt1 = this.database.preparedStatement("minecraft/user_get");
        ) {
            stmt0.setLong(1, action.getId());
            stmt1.setLong(1, action.getId());

            ResultSet  res = stmt0.executeQuery();
            ResultSet uRes = stmt1.executeQuery();

            if (!res.next())
                throw new NoSuchEntryException();

            return EntityProvider.getMinecraftAccount(action.getAPI(), action.getId(), res, uRes);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public BackendMinecraftAccount onMinecraftAccountBuild(@NotNull DatabaseMinecraftAccountBuilder builder) throws DatabaseException {
        long id = 0; // TODO: generate id

        try (PreparedStatement stmt = this.database.preparedStatement("minecraft/minecraft_create")) {
            stmt.setLong(1, id);
            stmt.setString(2, builder.getUUID().toString());
            stmt.setString(3, builder.getName());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        if (builder.getUser() != null) {
            // TODO: create link
        }

        return new BackendMinecraftAccount(builder.getAPI(), id, builder.getUUID(), builder.getName(), ((AbstractUser) builder.getUser()));
    }

    @Override
    public void onMinecraftAccountModify(@NotNull DatabaseMinecraftAccountModifier modifier) throws DatabaseException {
        String oldName = modifier.getEntity().getCachedName();
        String newName = modifier.getName();

        if (!Objects.equals(oldName, newName)) {
            try (PreparedStatement stmt = this.database.preparedStatement("minecraft/minecraft_update_name")) {
                stmt.setString(1, newName);

                stmt.setLong(2, modifier.getEntity().getId());

                stmt.execute();
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }

        User oldUser = modifier.getEntity().getUser();
        User newUser = modifier.getUser();

        if (!Objects.equals(oldUser, newUser)) {
            // TODO: create link
        }
    }

    @Override
    public void onMinecraftAccountDelete(@NotNull DatabaseMinecraftAccountDeleteAction deleteAction) throws DatabaseException {
        try (PreparedStatement stmt = this.database.preparedStatement("minecraft/minecraft_delete")) {
            stmt.setLong(1, deleteAction.getId());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
