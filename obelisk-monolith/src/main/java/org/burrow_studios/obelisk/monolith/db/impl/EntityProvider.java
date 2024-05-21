package org.burrow_studios.obelisk.monolith.db.impl;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.core.cache.OrderedEntitySetView;
import org.burrow_studios.obelisk.core.entities.AbstractDiscordAccount;
import org.burrow_studios.obelisk.core.entities.AbstractMinecraftAccount;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.entities.*;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.burrow_studios.obelisk.monolith.exceptions.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class EntityProvider {
    public static @NotNull BackendUser getUser(@NotNull ObeliskMonolith obelisk, long id, @NotNull ResultSet res, @NotNull ResultSet dRes, @NotNull ResultSet mRes) throws SQLException, DatabaseException {
        OrderedEntitySetView<AbstractDiscordAccount>   discordAccounts   = new OrderedEntitySetView<>(obelisk.getDiscordAccounts(),   AbstractDiscordAccount.class);
        OrderedEntitySetView<AbstractMinecraftAccount> minecraftAccounts = new OrderedEntitySetView<>(obelisk.getMinecraftAccounts(), AbstractMinecraftAccount.class);

        if (!res.next())
            throw new NoSuchEntryException();

        String name = res.getString("name");

        BackendUser user = new BackendUser(obelisk, id, name, discordAccounts, minecraftAccounts);

        while (dRes.next()) {
            final long   discordId        = dRes.getLong("id");
            final long   discordSnowflake = dRes.getLong("snowflake");
            final String discordName      = dRes.getString("name");
            final int    discordIndex     = dRes.getInt("index");

            AbstractDiscordAccount discordAccount = obelisk.getDiscordAccount(discordId);

            if (discordAccount == null)
                discordAccount = new BackendDiscordAccount(obelisk, discordId, discordSnowflake, discordName, user);

            discordAccounts.add(discordIndex, discordAccount);
        }

        while (mRes.next()) {
            final long   minecraftId    = mRes.getLong("id");
            final UUID   minecraftUUID  = UUID.fromString(mRes.getString("uuid"));
            final String minecraftName  = mRes.getString("name");
            final int    minecraftIndex = mRes.getInt("index");

            AbstractMinecraftAccount minecraftAccount = obelisk.getMinecraftAccount(minecraftId);

            if (minecraftAccount == null)
                minecraftAccount = new BackendMinecraftAccount(obelisk, minecraftId, minecraftUUID, minecraftName, user);

            minecraftAccounts.add(minecraftIndex, minecraftAccount);
        }

        return user;
    }

    public static @NotNull BackendTicket getTicket(@NotNull ObeliskMonolith obelisk, long id, @NotNull ResultSet res, @NotNull ResultSet uRes) throws SQLException, DatabaseException {
        OrderedEntitySetView<AbstractUser> users = new OrderedEntitySetView<>(obelisk.getUsers(), AbstractUser.class);

        if (!res.next())
            throw new NoSuchEntryException();

        String       title = res.getString("title");
        Ticket.State state = Ticket.State.valueOf(res.getString("state"));

        BackendTicket ticket = new BackendTicket(obelisk, id, title, state, users);

        while (uRes.next()) {
            final long   userId   = uRes.getLong("id");
            final String userName = uRes.getString("name");

            AbstractUser user = obelisk.getUser(userId);

            if (user == null) {
                OrderedEntitySetView<AbstractDiscordAccount>   discordAccounts   = new OrderedEntitySetView<>(obelisk.getDiscordAccounts(),   AbstractDiscordAccount.class);
                OrderedEntitySetView<AbstractMinecraftAccount> minecraftAccounts = new OrderedEntitySetView<>(obelisk.getMinecraftAccounts(), AbstractMinecraftAccount.class);

                // TODO: queue background job to fill discord & minecraft

                user = new BackendUser(obelisk, userId, userName, discordAccounts, minecraftAccounts);
            }

            users.add(user);
        }

        return ticket;
    }

    public static @NotNull BackendProject getProject(@NotNull ObeliskMonolith obelisk, long id, @NotNull ResultSet res, @NotNull ResultSet mRes) throws SQLException, DatabaseException {
        OrderedEntitySetView<AbstractUser> members = new OrderedEntitySetView<>(obelisk.getUsers(), AbstractUser.class);

        if (!res.next())
            throw new NoSuchEntryException();

        String        title = res.getString("title");
        Project.State state = Project.State.valueOf(res.getString("state"));

        BackendProject project = new BackendProject(obelisk, id, title, state, members);

        while (mRes.next()) {
            final long   userId   = mRes.getLong("id");
            final String userName = mRes.getString("name");

            AbstractUser user = obelisk.getUser(userId);

            if (user == null) {
                OrderedEntitySetView<AbstractDiscordAccount>   discordAccounts   = new OrderedEntitySetView<>(obelisk.getDiscordAccounts(),   AbstractDiscordAccount.class);
                OrderedEntitySetView<AbstractMinecraftAccount> minecraftAccounts = new OrderedEntitySetView<>(obelisk.getMinecraftAccounts(), AbstractMinecraftAccount.class);

                // TODO: queue background job to fill discord & minecraft

                user = new BackendUser(obelisk, userId, userName, discordAccounts, minecraftAccounts);
            }

            members.add(user);
        }

        return project;
    }
}
