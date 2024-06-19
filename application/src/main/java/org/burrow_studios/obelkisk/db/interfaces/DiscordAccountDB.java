package org.burrow_studios.obelkisk.db.interfaces;

import org.burrow_studios.obelkisk.entity.DatabaseDiscordAccount;
import org.burrow_studios.obelkisk.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DiscordAccountDB {
    @NotNull DatabaseDiscordAccount createDiscordAccount(long snowflake, @NotNull String name) throws DatabaseException;


    @NotNull List<DatabaseDiscordAccount> listDiscordAccounts() throws DatabaseException;

    @NotNull String getDiscordAccountName(long snowflake) throws DatabaseException;


    void setDiscordAccountName(long snowflake, @NotNull String name) throws DatabaseException;


    void deleteDiscordAccount(long snowflake) throws DatabaseException;
}
