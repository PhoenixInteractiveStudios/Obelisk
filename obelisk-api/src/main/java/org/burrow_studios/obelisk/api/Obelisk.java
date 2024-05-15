package org.burrow_studios.obelisk.api;

import org.burrow_studios.obelisk.api.action.entity.discord.MinecraftAccountBuilder;
import org.burrow_studios.obelisk.api.action.entity.minecraft.DiscordAccountBuilder;
import org.burrow_studios.obelisk.api.action.entity.project.ProjectBuilder;
import org.burrow_studios.obelisk.api.action.entity.ticket.TicketBuilder;
import org.burrow_studios.obelisk.api.action.entity.user.UserBuilder;
import org.burrow_studios.obelisk.api.cache.EntitySet;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface Obelisk {
    void awaitReady() throws InterruptedException;

    void awaitReady(long timeout, @NotNull TimeUnit unit) throws InterruptedException, TimeoutException;

    void awaitShutdown() throws InterruptedException;

    void awaitShutdown(long timeout, @NotNull TimeUnit unit) throws InterruptedException, TimeoutException;

    @NotNull EntitySet<? extends User> getUsers();

    @NotNull EntitySet<? extends Ticket> getTickets();

    @NotNull EntitySet<? extends Project> getProjects();

    @NotNull UserBuilder createUser();

    @NotNull TicketBuilder createTicket();

    @NotNull ProjectBuilder createProject();

    @NotNull DiscordAccountBuilder createDiscordAccount();

    @NotNull MinecraftAccountBuilder createMinecraftAccount();
}
