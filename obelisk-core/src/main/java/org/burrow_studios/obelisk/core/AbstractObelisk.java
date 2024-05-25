package org.burrow_studios.obelisk.core;

import org.burrow_studios.obelisk.api.Obelisk;
import org.burrow_studios.obelisk.api.Status;
import org.burrow_studios.obelisk.api.action.Action;
import org.burrow_studios.obelisk.core.cache.EntityCache;
import org.burrow_studios.obelisk.core.entities.*;
import org.burrow_studios.obelisk.util.EnumLatch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class AbstractObelisk implements Obelisk {
    private final @NotNull EntityCache<AbstractUser> userCache;
    private final @NotNull EntityCache<AbstractTicket> ticketCache;
    private final @NotNull EntityCache<AbstractProject> projectCache;

    private final @NotNull EntityCache<AbstractDiscordAccount> discordAccountCache;
    private final @NotNull EntityCache<AbstractMinecraftAccount> minecraftAccountCache;

    protected final @NotNull EnumLatch<Status> status;

    protected AbstractObelisk() {
        this.status = new EnumLatch<>(Status.PRE_INIT);

        this.userCache = new EntityCache<>(this, AbstractUser.class);
        this.ticketCache = new EntityCache<>(this, AbstractTicket.class);
        this.projectCache = new EntityCache<>(this, AbstractProject.class);

        this.discordAccountCache = new EntityCache<>(this, AbstractDiscordAccount.class);
        this.minecraftAccountCache = new EntityCache<>(this, AbstractMinecraftAccount.class);
    }

    public final @NotNull Status getStatus() {
        return this.status.get();
    }

    public final void setStatus(@NotNull Status status) {
        this.status.set(status);
    }

    @Override
    public final void awaitReady() throws InterruptedException {
        this.status.await(Status.READY);
    }

    @Override
    public final void awaitReady(long timeout, @NotNull TimeUnit unit) throws InterruptedException, TimeoutException {
        if (this.status.await(Status.READY, timeout, unit))
            throw new TimeoutException();
    }

    @Override
    public final void awaitShutdown() throws InterruptedException {
        this.status.await(Status.STOPPED);
    }

    @Override
    public final void awaitShutdown(long timeout, @NotNull TimeUnit unit) throws InterruptedException, TimeoutException {
        if (this.status.await(Status.STOPPED, timeout, unit))
            throw new TimeoutException();
    }

    @Override
    public final @NotNull EntityCache<AbstractUser> getUsers() {
        return this.userCache;
    }

    @Override
    public final @Nullable AbstractUser getUser(long id) {
        return this.userCache.get(id);
    }

    @Override
    public final @NotNull EntityCache<AbstractTicket> getTickets() {
        return this.ticketCache;
    }

    @Override
    public final @Nullable AbstractTicket getTicket(long id) {
        return this.ticketCache.get(id);
    }

    @Override
    public final @NotNull EntityCache<AbstractProject> getProjects() {
        return this.projectCache;
    }

    @Override
    public final @Nullable AbstractProject getProject(long id) {
        return this.projectCache.get(id);
    }

    @Override
    public final @NotNull EntityCache<AbstractDiscordAccount> getDiscordAccounts() {
        return this.discordAccountCache;
    }

    @Override
    public final @Nullable AbstractDiscordAccount getDiscordAccount(long id) {
        return this.discordAccountCache.get(id);
    }

    @Override
    public final @NotNull EntityCache<AbstractMinecraftAccount> getMinecraftAccounts() {
        return this.minecraftAccountCache;
    }

    @Override
    public final @Nullable AbstractMinecraftAccount getMinecraftAccount(long id) {
        return this.minecraftAccountCache.get(id);
    }

    @Override
    public abstract @NotNull Action<EntityCache<AbstractUser>> retrieveUsers();

    @Override
    public abstract @NotNull Action<EntityCache<AbstractTicket>> retrieveTickets();

    @Override
    public abstract @NotNull Action<EntityCache<AbstractProject>> retrieveProjects();

    @Override
    public abstract @NotNull Action<EntityCache<AbstractDiscordAccount>> retrieveDiscordAccounts();

    @Override
    public abstract @NotNull Action<EntityCache<AbstractMinecraftAccount>> retrieveMinecraftAccounts();

    @Override
    public abstract @NotNull Action<? extends AbstractUser> retrieveUser(long id);

    @Override
    public abstract @NotNull Action<? extends AbstractTicket> retrieveTicket(long id);

    @Override
    public abstract @NotNull Action<? extends AbstractProject> retrieveProject(long id);

    @Override
    public abstract @NotNull Action<? extends AbstractDiscordAccount> retrieveDiscordAccount(long id);

    @Override
    public abstract @NotNull Action<? extends AbstractMinecraftAccount> retrieveMinecraftAccount(long id);
}
