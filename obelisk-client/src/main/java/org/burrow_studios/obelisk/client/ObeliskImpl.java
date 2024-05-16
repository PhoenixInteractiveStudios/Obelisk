package org.burrow_studios.obelisk.client;

import org.burrow_studios.obelisk.api.action.Action;
import org.burrow_studios.obelisk.api.action.entity.discord.MinecraftAccountBuilder;
import org.burrow_studios.obelisk.api.action.entity.minecraft.DiscordAccountBuilder;
import org.burrow_studios.obelisk.api.action.entity.project.ProjectBuilder;
import org.burrow_studios.obelisk.api.action.entity.ticket.TicketBuilder;
import org.burrow_studios.obelisk.api.action.entity.user.UserBuilder;
import org.burrow_studios.obelisk.client.config.AuthConfig;
import org.burrow_studios.obelisk.client.config.HttpConfig;
import org.burrow_studios.obelisk.client.http.HTTPClient;
import org.burrow_studios.obelisk.core.AbstractObelisk;
import org.burrow_studios.obelisk.util.EnumLatch;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ObeliskImpl extends AbstractObelisk {
    private final AuthConfig authConfig;
    private final HttpConfig httpConfig;
    private final EntityBuilder entityBuilder;
    private final EnumLatch<Status> status;
    private HTTPClient httpClient;
    private URI gatewayUrl;

    public ObeliskImpl(@NotNull AuthConfig authConfig, @NotNull HttpConfig httpConfig) {
        this.status = new EnumLatch<>(Status.PRE_INIT);

        this.authConfig = authConfig;
        this.httpConfig = httpConfig;

        this.entityBuilder = new EntityBuilder(this);
    }

    public void login() {
        this.login(null);
    }

    public void login(URI gatewayUrl) {
        Status s = this.status.get();

        if (s != Status.PRE_INIT)
            throw new IllegalStateException("May not log in while status is " + s);
        this.status.set(Status.INIT);

        if (this.httpClient != null)
            throw new IllegalStateException("HTTPClient has already been initialized");
        this.httpClient = new HTTPClient(authConfig, httpConfig);

        this.gatewayUrl = gatewayUrl;
        if (this.gatewayUrl == null) {
            try {
                this.gatewayUrl = getGatewayUrl().await();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException("Failed to retrieve gateway url", e);
            }
        }

        // TODO: initial cache fill
    }

    public @NotNull Action<URI> getGatewayUrl() {
        // TODO: acquire gateway url
        return null;
    }

    public @NotNull Status getStatus() {
        return this.status.get();
    }

    public @NotNull HTTPClient getHttpClient() {
        return this.httpClient;
    }

    public @NotNull EntityBuilder getEntityBuilder() {
        return this.entityBuilder;
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
