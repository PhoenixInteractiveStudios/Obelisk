package org.burrow_studios.obelisk.client;

import org.burrow_studios.obelisk.api.action.entity.discord.MinecraftAccountBuilder;
import org.burrow_studios.obelisk.api.action.entity.minecraft.DiscordAccountBuilder;
import org.burrow_studios.obelisk.api.action.entity.project.ProjectBuilder;
import org.burrow_studios.obelisk.api.action.entity.ticket.TicketBuilder;
import org.burrow_studios.obelisk.api.action.entity.user.UserBuilder;
import org.burrow_studios.obelisk.core.AbstractObelisk;
import org.burrow_studios.obelisk.util.EnumLatch;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ObeliskImpl extends AbstractObelisk {
    private final EnumLatch<Status> status;

    public ObeliskImpl() {
        this.status = new EnumLatch<>(Status.PRE_INIT);
    }

    @Override
    public void awaitReady() throws InterruptedException {
        this.status.await(Status.READY);
    }

    @Override
    public void awaitReady(long timeout, @NotNull TimeUnit unit) throws InterruptedException, TimeoutException {
        if (this.status.await(Status.READY, timeout, unit))
            throw new TimeoutException();
    }

    @Override
    public void awaitShutdown() throws InterruptedException {
        this.status.await(Status.STOPPED);
    }

    @Override
    public void awaitShutdown(long timeout, @NotNull TimeUnit unit) throws InterruptedException, TimeoutException {
        if (this.status.await(Status.STOPPED, timeout, unit))
            throw new TimeoutException();
    }

    @Override
    public @NotNull UserBuilder createUser() {
        // TODO
        return null;
    }

    @Override
    public @NotNull TicketBuilder createTicket() {
        // TODO
        return null;
    }

    @Override
    public @NotNull ProjectBuilder createProject() {
        // TODO
        return null;
    }

    @Override
    public @NotNull DiscordAccountBuilder createDiscordAccount() {
        // TODO
        return null;
    }

    @Override
    public @NotNull MinecraftAccountBuilder createMinecraftAccount() {
        // TODO
        return null;
    }
}
