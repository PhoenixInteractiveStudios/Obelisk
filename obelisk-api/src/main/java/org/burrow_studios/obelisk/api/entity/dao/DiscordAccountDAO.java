package org.burrow_studios.obelisk.api.entity.dao;

import org.burrow_studios.obelisk.api.entity.DiscordAccount;
import org.burrow_studios.obelisk.api.entity.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface DiscordAccountDAO {
    @NotNull DiscordAccount createDiscordAccount(long snowflake, @NotNull String name);

    @NotNull List<? extends DiscordAccount> listDiscordAccounts();
    @NotNull DiscordAccount getDiscordAccount(long snowflake);
    @Nullable User getDiscordAccountUser(long snowflake);
    @NotNull String getDiscordAccountName(long snowflake);

    void setDiscordAccountUser(long snowflake, @Nullable User user);
    void setDiscordAccountName(long snowflake, @NotNull String name);

    void deleteDiscordAccount(long snowflake);
}
