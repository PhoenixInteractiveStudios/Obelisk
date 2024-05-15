package org.burrow_studios.obelisk.core;

import org.burrow_studios.obelisk.api.Obelisk;
import org.burrow_studios.obelisk.core.cache.EntityCache;
import org.burrow_studios.obelisk.core.entities.*;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractObelisk implements Obelisk {
    private final @NotNull EntityCache<AbstractUser> userCache;
    private final @NotNull EntityCache<AbstractTicket> ticketCache;
    private final @NotNull EntityCache<AbstractProject> projectCache;

    private final @NotNull EntityCache<AbstractDiscordAccount> discordAccountCache;
    private final @NotNull EntityCache<AbstractMinecraftAccount> minecraftAccountCache;

    protected AbstractObelisk() {
        this.userCache = new EntityCache<>(this, AbstractUser.class);
        this.ticketCache = new EntityCache<>(this, AbstractTicket.class);
        this.projectCache = new EntityCache<>(this, AbstractProject.class);

        this.discordAccountCache = new EntityCache<>(this, AbstractDiscordAccount.class);
        this.minecraftAccountCache = new EntityCache<>(this, AbstractMinecraftAccount.class);
    }

    @Override
    public final @NotNull EntityCache<AbstractUser> getUsers() {
        return this.userCache;
    }

    @Override
    public final @NotNull EntityCache<AbstractTicket> getTickets() {
        return this.ticketCache;
    }

    @Override
    public final @NotNull EntityCache<AbstractProject> getProjects() {
        return this.projectCache;
    }

    public final @NotNull EntityCache<AbstractDiscordAccount> getDiscordAccounts() {
        return this.discordAccountCache;
    }

    public final @NotNull EntityCache<AbstractMinecraftAccount> getMinecraftAccounts() {
        return this.minecraftAccountCache;
    }
}
