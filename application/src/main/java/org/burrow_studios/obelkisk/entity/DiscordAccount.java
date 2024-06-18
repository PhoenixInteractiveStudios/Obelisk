package org.burrow_studios.obelkisk.entity;

import org.burrow_studios.obelkisk.db.interfaces.DiscordAccountDB;
import org.jetbrains.annotations.NotNull;

public final class DiscordAccount extends AbstractEntity {
    private final DiscordAccountDB database;

    public DiscordAccount(long id, @NotNull DiscordAccountDB database) {
        super(id);
        this.database = database;
    }

    public long getSnowflake() {
        return this.database.getDiscordAccountSnowflake(this.id);
    }

    public @NotNull String getName() {
        return this.database.getDiscordAccountName(this.id);
    }

    public void setName(@NotNull String name) {
        this.database.setDiscordAccountName(this.id, name);
    }

    @Override
    public void delete() {
        this.database.deleteDiscordAccount(this.id);
    }
}
