package org.burrow_studios.obelkisk.db.interfaces;

import org.burrow_studios.obelkisk.entity.DiscordAccount;
import org.burrow_studios.obelkisk.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DiscordAccountDB {
    @NotNull DiscordAccount createDiscordAccount(long snowflake, @NotNull String name) throws DatabaseException;


    @NotNull List<DiscordAccount> listDiscordAccounts() throws DatabaseException;

    long getDiscordAccountSnowflake(long id) throws DatabaseException;

    @NotNull String getDiscordAccountName(long id) throws DatabaseException;


    void setDiscordAccountName(long id, @NotNull String name) throws DatabaseException;


    void deleteDiscordAccount(long id) throws DatabaseException;
}
