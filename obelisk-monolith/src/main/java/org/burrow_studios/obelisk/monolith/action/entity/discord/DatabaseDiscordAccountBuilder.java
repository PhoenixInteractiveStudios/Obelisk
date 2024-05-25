package org.burrow_studios.obelisk.monolith.action.entity.discord;

import org.burrow_studios.obelisk.api.action.entity.minecraft.DiscordAccountBuilder;
import org.burrow_studios.obelisk.api.entities.DiscordAccount;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseBuilder;
import org.burrow_studios.obelisk.monolith.db.interfaces.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendDiscordAccount;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DatabaseDiscordAccountBuilder extends DatabaseBuilder<DiscordAccount> implements DiscordAccountBuilder {
    private Long snowflake;
    private String name;
    private User user;

    public DatabaseDiscordAccountBuilder(@NotNull ObeliskMonolith obelisk) {
        super(obelisk);
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<DiscordAccount> future) throws DatabaseException {
        BackendDiscordAccount discordAccount = actionableDatabase.createDiscordAccount(this);
        future.complete(discordAccount);
    }

    @Override
    public @NotNull DatabaseDiscordAccountBuilder setSnowflake(long snowflake) {
        this.snowflake = snowflake;
        return this;
    }

    public Long getSnowflake() {
        return this.snowflake;
    }

    @Override
    public @NotNull DatabaseDiscordAccountBuilder setCachedName(@NotNull String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public @NotNull DatabaseDiscordAccountBuilder setUser(@Nullable User user) {
        this.user = user;
        return this;
    }

    public User getUser() {
        return this.user;
    }
}
