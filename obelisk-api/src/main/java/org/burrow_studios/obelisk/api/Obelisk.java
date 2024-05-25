package org.burrow_studios.obelisk.api;

import org.burrow_studios.obelisk.api.action.Action;
import org.burrow_studios.obelisk.api.action.entity.discord.MinecraftAccountBuilder;
import org.burrow_studios.obelisk.api.action.entity.minecraft.DiscordAccountBuilder;
import org.burrow_studios.obelisk.api.action.entity.project.ProjectBuilder;
import org.burrow_studios.obelisk.api.action.entity.ticket.TicketBuilder;
import org.burrow_studios.obelisk.api.action.entity.user.UserBuilder;
import org.burrow_studios.obelisk.api.cache.EntitySet;
import org.burrow_studios.obelisk.api.entities.*;
import org.burrow_studios.obelisk.api.event.EventManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface Obelisk {
    @NotNull EventManager getEventManager();

    @NotNull Status getStatus();

    void awaitReady() throws InterruptedException;

    void awaitReady(long timeout, @NotNull TimeUnit unit) throws InterruptedException, TimeoutException;

    void awaitShutdown() throws InterruptedException;

    void awaitShutdown(long timeout, @NotNull TimeUnit unit) throws InterruptedException, TimeoutException;

    @NotNull EntitySet<? extends User> getUsers();

    @Nullable User getUser(long id);

    @NotNull EntitySet<? extends Ticket> getTickets();

    @Nullable Ticket getTicket(long id);

    @NotNull EntitySet<? extends Project> getProjects();

    @Nullable Project getProject(long id);

    @NotNull EntitySet<? extends DiscordAccount> getDiscordAccounts();

    @Nullable DiscordAccount getDiscordAccount(long id);

    @NotNull EntitySet<? extends MinecraftAccount> getMinecraftAccounts();

    @Nullable MinecraftAccount getMinecraftAccount(long id);

    @NotNull Action<? extends EntitySet<? extends User>> retrieveUsers();

    @NotNull Action<? extends EntitySet<? extends Ticket>> retrieveTickets();

    @NotNull Action<? extends EntitySet<? extends Project>> retrieveProjects();

    @NotNull Action<? extends EntitySet<? extends DiscordAccount>> retrieveDiscordAccounts();

    @NotNull Action<? extends EntitySet<? extends MinecraftAccount>> retrieveMinecraftAccounts();

    @NotNull Action<? extends User> retrieveUser(long id);

    @NotNull Action<? extends Ticket> retrieveTicket(long id);

    @NotNull Action<? extends Project> retrieveProject(long id);

    @NotNull Action<? extends DiscordAccount> retrieveDiscordAccount(long id);

    @NotNull Action<? extends MinecraftAccount> retrieveMinecraftAccount(long id);

    @NotNull UserBuilder createUser();

    @NotNull TicketBuilder createTicket();

    @NotNull ProjectBuilder createProject();

    @NotNull DiscordAccountBuilder createDiscordAccount();

    @NotNull MinecraftAccountBuilder createMinecraftAccount();
}
