package org.burrow_studios.obelisk.monolith.db.impl;

import org.burrow_studios.obelisk.core.cache.OrderedEntitySetView;
import org.burrow_studios.obelisk.core.entities.AbstractDiscordAccount;
import org.burrow_studios.obelisk.core.entities.AbstractMinecraftAccount;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.entities.BackendDiscordAccount;
import org.burrow_studios.obelisk.monolith.entities.BackendMinecraftAccount;
import org.burrow_studios.obelisk.monolith.entities.BackendUser;
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
}
