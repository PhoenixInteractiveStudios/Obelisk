package org.burrow_studios.obelkisk.core.entity;

import org.burrow_studios.obelisk.api.entity.DiscordAccount;
import org.burrow_studios.obelkisk.core.db.interfaces.DiscordAccountDB;
import org.jetbrains.annotations.NotNull;

public final class DatabaseDiscordAccount implements DiscordAccount {
    private final long snowflake;
    private final DiscordAccountDB database;

    public DatabaseDiscordAccount(long snowflake, @NotNull DiscordAccountDB database) {
        this.snowflake = snowflake;
        this.database = database;
    }

    public long getSnowflake() {
        return this.snowflake;
    }

    public @NotNull String getName() {
        return this.database.getDiscordAccountName(this.snowflake);
    }

    public void setName(@NotNull String name) {
        this.database.setDiscordAccountName(this.snowflake, name);
    }

    public void delete() {
        this.database.deleteDiscordAccount(this.snowflake);
    }
}
