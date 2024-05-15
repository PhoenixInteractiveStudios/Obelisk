package org.burrow_studios.obelisk.core.entities;

import org.burrow_studios.obelisk.api.entities.DiscordAccount;
import org.burrow_studios.obelisk.core.AbstractObelisk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDiscordAccount extends AbstractEntity implements DiscordAccount {
    private final long snowflake;
    private @NotNull String cachedName;
    private @Nullable AbstractUser user;

    protected AbstractDiscordAccount(
            @NotNull AbstractObelisk obelisk,
            long id,
            long snowflake,
            @NotNull String cachedName,
            @Nullable AbstractUser user
    ) {
        super(obelisk, id);
        this.snowflake = snowflake;
        this.cachedName = cachedName;
        this.user = user;
    }

    @Override
    public final long getSnowflake() {
        return this.snowflake;
    }

    @Override
    public final @NotNull String getCachedName() {
        return this.cachedName;
    }

    public final void setCachedName(@NotNull String cachedName) {
        this.cachedName = cachedName;
    }

    @Override
    public @Nullable AbstractUser getUser() {
        return this.user;
    }

    public void setUser(@Nullable AbstractUser user) {
        this.user = user;
    }
}
