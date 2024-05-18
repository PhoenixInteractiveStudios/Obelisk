package org.burrow_studios.obelisk.client;

import org.burrow_studios.obelisk.api.Status;
import org.burrow_studios.obelisk.api.action.Action;
import org.burrow_studios.obelisk.client.action.entity.discord.DiscordAccountBuilderImpl;
import org.burrow_studios.obelisk.client.action.entity.minecraft.MinecraftAccountBuilderImpl;
import org.burrow_studios.obelisk.client.action.entity.project.ProjectBuilderImpl;
import org.burrow_studios.obelisk.client.action.entity.ticket.TicketBuilderImpl;
import org.burrow_studios.obelisk.client.action.entity.user.UserBuilderImpl;
import org.burrow_studios.obelisk.client.config.AuthConfig;
import org.burrow_studios.obelisk.client.config.HttpConfig;
import org.burrow_studios.obelisk.client.http.HTTPClient;
import org.burrow_studios.obelisk.core.AbstractObelisk;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.concurrent.ExecutionException;

public class ObeliskImpl extends AbstractObelisk {
    private final AuthConfig authConfig;
    private final HttpConfig httpConfig;
    private final EntityBuilder entityBuilder;
    private HTTPClient httpClient;
    private URI gatewayUrl;

    public ObeliskImpl(@NotNull AuthConfig authConfig, @NotNull HttpConfig httpConfig) {
        super();

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

    public @NotNull HTTPClient getHttpClient() {
        return this.httpClient;
    }

    public @NotNull EntityBuilder getEntityBuilder() {
        return this.entityBuilder;
    }

    @Override
    public @NotNull UserBuilderImpl createUser() {
        return new UserBuilderImpl(this);
    }

    @Override
    public @NotNull TicketBuilderImpl createTicket() {
        return new TicketBuilderImpl(this);
    }

    @Override
    public @NotNull ProjectBuilderImpl createProject() {
        return new ProjectBuilderImpl(this);
    }

    @Override
    public @NotNull DiscordAccountBuilderImpl createDiscordAccount() {
        return new DiscordAccountBuilderImpl(this);
    }

    @Override
    public @NotNull MinecraftAccountBuilderImpl createMinecraftAccount() {
        return new MinecraftAccountBuilderImpl(this);
    }
}
