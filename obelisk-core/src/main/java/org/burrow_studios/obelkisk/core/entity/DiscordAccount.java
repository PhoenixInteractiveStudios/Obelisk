package org.burrow_studios.obelkisk.core.entity;

import org.burrow_studios.obelkisk.core.db.interfaces.DiscordAccountDB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DiscordAccount {
    private final long snowflake;
    private final DiscordAccountDB database;

    public DiscordAccount(long snowflake, @NotNull DiscordAccountDB database) {
        this.snowflake = snowflake;
        this.database = database;
    }

    public long getSnowflake() {
        return this.snowflake;
    }

    public @Nullable User getUser() {
        return this.database.getDiscordAccountUser(this.snowflake);
    }

    public @NotNull String getName() {
        return this.database.getDiscordAccountName(this.snowflake);
    }

    public void setUser(@Nullable User user) {
        this.database.setDiscordAccountUser(this.snowflake, user);
    }

    public void setName(@NotNull String name) {
        this.database.setDiscordAccountName(this.snowflake, name);
    }

    public void delete() {
        this.database.deleteDiscordAccount(this.snowflake);
    }
}
