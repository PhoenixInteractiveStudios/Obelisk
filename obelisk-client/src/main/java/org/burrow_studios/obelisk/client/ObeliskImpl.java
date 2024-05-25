package org.burrow_studios.obelisk.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.Status;
import org.burrow_studios.obelisk.api.action.Action;
import org.burrow_studios.obelisk.client.action.ActionImpl;
import org.burrow_studios.obelisk.client.action.entity.discord.DiscordAccountBuilderImpl;
import org.burrow_studios.obelisk.client.action.entity.minecraft.MinecraftAccountBuilderImpl;
import org.burrow_studios.obelisk.client.action.entity.project.ProjectBuilderImpl;
import org.burrow_studios.obelisk.client.action.entity.ticket.TicketBuilderImpl;
import org.burrow_studios.obelisk.client.action.entity.user.UserBuilderImpl;
import org.burrow_studios.obelisk.client.config.AuthConfig;
import org.burrow_studios.obelisk.client.config.HttpConfig;
import org.burrow_studios.obelisk.client.config.GatewayConfig;
import org.burrow_studios.obelisk.client.entities.*;
import org.burrow_studios.obelisk.client.http.HTTPClient;
import org.burrow_studios.obelisk.core.AbstractObelisk;
import org.burrow_studios.obelisk.core.cache.EntityCache;
import org.burrow_studios.obelisk.core.entities.*;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.concurrent.ExecutionException;

public class ObeliskImpl extends AbstractObelisk {
    private final AuthConfig authConfig;
    private final HttpConfig httpConfig;
    private final GatewayConfig gatewayConfig;
    private final EntityBuilder entityBuilder;
    private HTTPClient httpClient;

    public ObeliskImpl(@NotNull AuthConfig authConfig, @NotNull HttpConfig httpConfig, @NotNull GatewayConfig gatewayConfig) {
        super();

        this.authConfig = authConfig;
        this.httpConfig = httpConfig;
        this.gatewayConfig = gatewayConfig;

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

        if (this.gatewayConfig.isEmpty()) {
            try {
                getGatewayUrl().await();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException("Failed to retrieve gateway url", e);
            }
        }

        // TODO: initial cache fill
    }

    public @NotNull Action<Void> getGatewayUrl() {
        // TODO: acquire gateway url & edit socketConfig
        return null;
    }

    public @NotNull HTTPClient getHttpClient() {
        return this.httpClient;
    }

    public @NotNull EntityBuilder getEntityBuilder() {
        return this.entityBuilder;
    }

    @Override
    public @NotNull Action<EntityCache<AbstractUser>> retrieveUsers() {
        return ActionImpl.simpleGet(this, Route.User.LIST_USERS.compile(), (request, response) -> {
            JsonElement body = response.getBodyJson();

            // TODO: handle error response?

            if (!(body instanceof JsonArray bodyArr))
                throw new IllegalArgumentException("Unexpected body type");

            this.getUsers().clear();

            for (JsonElement element : bodyArr) {
                if (!(element instanceof JsonObject elementObj))
                    throw new IllegalArgumentException("Unexpected element type");

                this.getUsers().add(this.entityBuilder.buildUser(elementObj));
            }

            return this.getUsers();
        });
    }

    @Override
    public @NotNull Action<EntityCache<AbstractTicket>> retrieveTickets() {
        return ActionImpl.simpleGet(this, Route.Ticket.LIST_TICKETS.compile(), (request, response) -> {
            JsonElement body = response.getBodyJson();

            // TODO: handle error response?

            if (!(body instanceof JsonArray bodyArr))
                throw new IllegalArgumentException("Unexpected body type");

            this.getTickets().clear();

            for (JsonElement element : bodyArr) {
                if (!(element instanceof JsonObject elementObj))
                    throw new IllegalArgumentException("Unexpected element type");

                this.getTickets().add(this.entityBuilder.buildTicket(elementObj));
            }

            return this.getTickets();
        });
    }

    @Override
    public @NotNull Action<EntityCache<AbstractProject>> retrieveProjects() {
        return ActionImpl.simpleGet(this, Route.Project.LIST_PROJECTS.compile(), (request, response) -> {
            JsonElement body = response.getBodyJson();

            // TODO: handle error response?

            if (!(body instanceof JsonArray bodyArr))
                throw new IllegalArgumentException("Unexpected body type");

            this.getProjects().clear();

            for (JsonElement element : bodyArr) {
                if (!(element instanceof JsonObject elementObj))
                    throw new IllegalArgumentException("Unexpected element type");

                this.getProjects().add(this.entityBuilder.buildProject(elementObj));
            }

            return this.getProjects();
        });
    }

    @Override
    public @NotNull Action<EntityCache<AbstractDiscordAccount>> retrieveDiscordAccounts() {
        return ActionImpl.simpleGet(this, Route.Discord.LIST_DISCORD_ACCOUNTS.compile(), (request, response) -> {
            JsonElement body = response.getBodyJson();

            // TODO: handle error response?

            if (!(body instanceof JsonArray bodyArr))
                throw new IllegalArgumentException("Unexpected body type");

            this.getDiscordAccounts().clear();

            for (JsonElement element : bodyArr) {
                if (!(element instanceof JsonObject elementObj))
                    throw new IllegalArgumentException("Unexpected element type");

                this.getDiscordAccounts().add(this.entityBuilder.buildDiscordAccount(elementObj));
            }

            return this.getDiscordAccounts();
        });
    }

    @Override
    public @NotNull Action<EntityCache<AbstractMinecraftAccount>> retrieveMinecraftAccounts() {
        return ActionImpl.simpleGet(this, Route.Minecraft.LIST_MINECRAFT_ACCOUNTS.compile(), (request, response) -> {
            JsonElement body = response.getBodyJson();

            // TODO: handle error response?

            if (!(body instanceof JsonArray bodyArr))
                throw new IllegalArgumentException("Unexpected body type");

            this.getMinecraftAccounts().clear();

            for (JsonElement element : bodyArr) {
                if (!(element instanceof JsonObject elementObj))
                    throw new IllegalArgumentException("Unexpected element type");

                this.getMinecraftAccounts().add(this.entityBuilder.buildMinecraftAccount(elementObj));
            }

            return this.getMinecraftAccounts();
        });
    }

    @Override
    public @NotNull Action<UserImpl> retrieveUser(long id) {
        return ActionImpl.simpleGet(this, Route.User.GET_USER.compile(id), (request, response) -> {
            JsonElement body = response.getBodyJson();

            // TODO: handle error response?

            if (!(body instanceof JsonObject bodyObj))
                throw new IllegalArgumentException("Unexpected body type");

            return this.entityBuilder.buildUser(bodyObj);
        });
    }

    @Override
    public @NotNull Action<TicketImpl> retrieveTicket(long id) {
        return ActionImpl.simpleGet(this, Route.Ticket.GET_TICKET.compile(id), (request, response) -> {
            JsonElement body = response.getBodyJson();

            // TODO: handle error response?

            if (!(body instanceof JsonObject bodyObj))
                throw new IllegalArgumentException("Unexpected body type");

            return this.entityBuilder.buildTicket(bodyObj);
        });
    }

    @Override
    public @NotNull Action<ProjectImpl> retrieveProject(long id) {
        return ActionImpl.simpleGet(this, Route.Project.GET_PROJECT.compile(id), (request, response) -> {
            JsonElement body = response.getBodyJson();

            // TODO: handle error response?

            if (!(body instanceof JsonObject bodyObj))
                throw new IllegalArgumentException("Unexpected body type");

            return this.entityBuilder.buildProject(bodyObj);
        });
    }

    @Override
    public @NotNull Action<DiscordAccountImpl> retrieveDiscordAccount(long id) {
        return ActionImpl.simpleGet(this, Route.Discord.GET_DISCORD_ACCOUNT.compile(id), (request, response) -> {
            JsonElement body = response.getBodyJson();

            // TODO: handle error response?

            if (!(body instanceof JsonObject bodyObj))
                throw new IllegalArgumentException("Unexpected body type");

            return this.entityBuilder.buildDiscordAccount(bodyObj);
        });
    }

    @Override
    public @NotNull Action<MinecraftAccountImpl> retrieveMinecraftAccount(long id) {
        return ActionImpl.simpleGet(this, Route.Minecraft.GET_MINECRAFT_ACCOUNT.compile(id), (request, response) -> {
            JsonElement body = response.getBodyJson();

            // TODO: handle error response?

            if (!(body instanceof JsonObject bodyObj))
                throw new IllegalArgumentException("Unexpected body type");

            return this.entityBuilder.buildMinecraftAccount(bodyObj);
        });
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
