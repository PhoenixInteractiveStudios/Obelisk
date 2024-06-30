package org.burrow_studios.obelkisk.core.db.interfaces;

import org.burrow_studios.obelkisk.core.entity.User;
import org.burrow_studios.obelkisk.core.exceptions.DatabaseException;
import org.burrow_studios.obelkisk.core.entity.DiscordAccount;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface DiscordAccountDB {
    @NotNull DiscordAccount createDiscordAccount(long snowflake, @NotNull String name) throws DatabaseException;


    @NotNull List<DiscordAccount> listDiscordAccounts() throws DatabaseException;

    @NotNull DiscordAccount getDiscordAccount(long snowflake) throws DatabaseException;

    @Nullable User getDiscordAccountUser(long snowflake) throws DatabaseException;

    @NotNull String getDiscordAccountName(long snowflake) throws DatabaseException;


    void setDiscordAccountUser(long snowflake, @Nullable User user) throws DatabaseException;

    void setDiscordAccountName(long snowflake, @NotNull String name) throws DatabaseException;


    void deleteDiscordAccount(long snowflake) throws DatabaseException;
}
