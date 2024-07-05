package org.burrow_studios.obelkisk.server.db.interfaces;

import org.burrow_studios.obelisk.api.entity.User;
import org.burrow_studios.obelkisk.server.entity.DatabaseUser;
import org.burrow_studios.obelkisk.server.exceptions.DatabaseException;
import org.burrow_studios.obelkisk.server.entity.DatabaseDiscordAccount;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface DiscordAccountDB {
    @NotNull DatabaseDiscordAccount createDiscordAccount(long snowflake, @NotNull String name) throws DatabaseException;


    @NotNull List<DatabaseDiscordAccount> listDiscordAccounts() throws DatabaseException;

    @NotNull DatabaseDiscordAccount getDiscordAccount(long snowflake) throws DatabaseException;

    @Nullable DatabaseUser getDiscordAccountUser(long snowflake) throws DatabaseException;

    @NotNull String getDiscordAccountName(long snowflake) throws DatabaseException;


    void setDiscordAccountUser(long snowflake, @Nullable User user) throws DatabaseException;

    void setDiscordAccountName(long snowflake, @NotNull String name) throws DatabaseException;


    void deleteDiscordAccount(long snowflake) throws DatabaseException;
}
