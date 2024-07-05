package org.burrow_studios.obelkisk.core.entity;

import org.burrow_studios.obelisk.api.entity.DiscordAccount;
import org.burrow_studios.obelisk.api.entity.User;
import org.burrow_studios.obelkisk.core.db.interfaces.DiscordAccountDB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DatabaseDiscordAccount implements DiscordAccount {
    private final long snowflake;
    private final DiscordAccountDB database;

    public DatabaseDiscordAccount(long snowflake, @NotNull DiscordAccountDB database) {
        this.snowflake = snowflake;
        this.database = database;
    }

    @Override
    public long getSnowflake() {
        return this.snowflake;
    }

    @Override
    public @Nullable DatabaseUser getUser() {
        return this.database.getDiscordAccountUser(this.snowflake);
    }

    @Override
    public @NotNull String getName() {
        return this.database.getDiscordAccountName(this.snowflake);
    }

    @Override
    public void setUser(@Nullable User user) {
        this.database.setDiscordAccountUser(this.snowflake, user);
    }

    @Override
    public void setName(@NotNull String name) {
        this.database.setDiscordAccountName(this.snowflake, name);
    }

    @Override
    public void delete() {
        this.database.deleteDiscordAccount(this.snowflake);
    }
}
