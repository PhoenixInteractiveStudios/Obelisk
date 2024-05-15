package org.burrow_studios.obelisk.core.entities;

import org.burrow_studios.obelisk.api.entities.MinecraftAccount;
import org.burrow_studios.obelisk.core.AbstractObelisk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class AbstractMinecraftAccount extends AbstractEntity implements MinecraftAccount {
    private final @NotNull UUID uuid;
    private @NotNull String cachedName;
    private @Nullable AbstractUser user;

    protected AbstractMinecraftAccount(
            @NotNull AbstractObelisk obelisk,
            long id,
            @NotNull UUID uuid,
            @NotNull String cachedName,
            @Nullable AbstractUser user
    ) {
        super(obelisk, id);
        this.uuid = uuid;
        this.cachedName = cachedName;
        this.user = user;
    }

    @Override
    public final @NotNull UUID getUUID() {
        return this.uuid;
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
