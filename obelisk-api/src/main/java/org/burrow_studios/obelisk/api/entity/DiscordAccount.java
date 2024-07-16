package org.burrow_studios.obelisk.api.entity;

import org.burrow_studios.obelisk.api.entity.dao.DiscordAccountDAO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DiscordAccount {
    private final long snowflake;
    private final DiscordAccountDAO dao;

    public DiscordAccount(long snowflake, @NotNull DiscordAccountDAO dao) {
        this.snowflake = snowflake;
        this.dao = dao;
    }

    public long getSnowflake() {
        return this.snowflake;
    }

    public @Nullable User getUser() {
        return this.dao.getDiscordAccountUser(this.snowflake);
    }

    public @NotNull String getName() {
        return this.dao.getDiscordAccountName(this.snowflake);
    }

    public void setUser(@Nullable User user) {
        this.dao.setDiscordAccountUser(this.snowflake, user);
    }

    public void setName(@NotNull String name) {
        this.dao.setDiscordAccountName(this.snowflake, name);
    }

    public void delete() {
        this.dao.deleteDiscordAccount(this.snowflake);
    }
}