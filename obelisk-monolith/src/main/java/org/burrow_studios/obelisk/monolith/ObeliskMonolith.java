package org.burrow_studios.obelisk.monolith;

import org.burrow_studios.obelisk.core.AbstractObelisk;
import org.burrow_studios.obelisk.core.http.Route;
import org.burrow_studios.obelisk.core.socket.Opcode;
import org.burrow_studios.obelisk.core.socket.SocketServer;
import org.burrow_studios.obelisk.monolith.action.entity.discord.DatabaseDiscordAccountBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.discord.DatabaseDiscordAccountGetAction;
import org.burrow_studios.obelisk.monolith.action.entity.discord.DatabaseDiscordAccountListAction;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.DatabaseMinecraftAccountBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.DatabaseMinecraftAccountGetAction;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.DatabaseMinecraftAccountListAction;
import org.burrow_studios.obelisk.monolith.action.entity.project.DatabaseProjectBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.project.DatabaseProjectGetAction;
import org.burrow_studios.obelisk.monolith.action.entity.project.DatabaseProjectListAction;
import org.burrow_studios.obelisk.monolith.action.entity.ticket.DatabaseTicketBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.ticket.DatabaseTicketGetAction;
import org.burrow_studios.obelisk.monolith.action.entity.ticket.DatabaseTicketListAction;
import org.burrow_studios.obelisk.monolith.action.entity.user.DatabaseUserBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.user.DatabaseUserGetAction;
import org.burrow_studios.obelisk.monolith.action.entity.user.DatabaseUserListAction;
import org.burrow_studios.obelisk.monolith.auth.AuthManager;
import org.burrow_studios.obelisk.monolith.db.DatabaseAdapter;
import org.burrow_studios.obelisk.monolith.db.impl.ActionableDatabase;
import org.burrow_studios.obelisk.monolith.db.impl.EntityDatabase;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.burrow_studios.obelisk.monolith.http.HTTPServer;
import org.burrow_studios.obelisk.monolith.http.handlers.*;
import org.burrow_studios.obelisk.monolith.socket.PacketHandler;
import org.burrow_studios.obelisk.util.EnvUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ObeliskMonolith extends AbstractObelisk {
    private static final Logger LOG = LoggerFactory.getLogger(ObeliskMonolith.class);

    private final AuthManager authManager;
    private final DatabaseAdapter databaseAdapter;
    private final HTTPServer apiServer;
    private final SocketServer socketServer;

    public ObeliskMonolith() throws DatabaseException, IOException {
        super();

        this.authManager = new AuthManager();

        final EntityDatabase entityDatabase = new EntityDatabase(this, new File(Main.DIR, "entities.db"));
        final ActionableDatabase database = new ActionableDatabase(entityDatabase);
        this.databaseAdapter = new DatabaseAdapter(database);

        this.socketServer = new SocketServer(EnvUtil.getInt("SOCKET_PORT", 8081));

        final PacketHandler packetHandler = new PacketHandler(this);
        this.socketServer.addHandler(Opcode.DISCONNECT, packetHandler::onDisconnect);
        this.socketServer.addHandler(Opcode.IDENTIFY, packetHandler::onIdentify);
        this.socketServer.addHandler(Opcode.HEARTBEAT, packetHandler::onHeartbeat);
        this.socketServer.addHandler(Opcode.CACHE_REQUEST, packetHandler::onCacheRequest);

        this.socketServer.addHandler(Opcode.HELLO, packetHandler::onUnexpected);
        this.socketServer.addHandler(Opcode.HEARTBEAT_ACK, packetHandler::onUnexpected);
        this.socketServer.addHandler(Opcode.CREATE_EVENT, packetHandler::onUnexpected);
        this.socketServer.addHandler(Opcode.DELETE_EVENT, packetHandler::onUnexpected);
        this.socketServer.addHandler(Opcode.UPDATE_EVENT, packetHandler::onUnexpected);
        this.socketServer.addHandler(Opcode.ENTITY_DATA, packetHandler::onUnexpected);
        this.socketServer.addHandler(Opcode.CACHE_DONE, packetHandler::onUnexpected);

        this.apiServer = new HTTPServer(EnvUtil.getInt("API_PORT", 8080), authManager);

        final GatewayHandler gatewayHandler = new GatewayHandler();
        this.apiServer.addHandler(Route.Meta.GET_GATEWAY, gatewayHandler::onGet, false);

        final UserHandler userHandler = new UserHandler(this);
        this.apiServer.addHandler(Route.User.GET_USER,    userHandler::onGet,    true, "user.read");
        this.apiServer.addHandler(Route.User.LIST_USERS,  userHandler::onList,   true, "user.read");
        this.apiServer.addHandler(Route.User.CREATE_USER, userHandler::onPost,   true, "user.read", "user.write");
        this.apiServer.addHandler(Route.User.EDIT_USER,   userHandler::onPatch,  true, "user.read", "user.write");
        this.apiServer.addHandler(Route.User.DELETE_USER, userHandler::onDelete, true, "user.write");

        final TicketHandler ticketHandler = new TicketHandler(this);
        this.apiServer.addHandler(Route.Ticket.GET_TICKET,    ticketHandler::onGet,    true, "ticket.read", "user.read");
        this.apiServer.addHandler(Route.Ticket.LIST_TICKETS,  ticketHandler::onList,   true, "ticket.read");
        this.apiServer.addHandler(Route.Ticket.CREATE_TICKET, ticketHandler::onPost,   true, "ticket.read", "ticket.write", "user.read", "user.write");
        this.apiServer.addHandler(Route.Ticket.EDIT_TICKET,   ticketHandler::onPatch,  true, "ticket.read", "ticket.write", "user.read", "user.write");
        this.apiServer.addHandler(Route.Ticket.DELETE_TICKET, ticketHandler::onDelete, true, "ticket.write", "user.write");

        final ProjectHandler projectHandler = new ProjectHandler(this);
        this.apiServer.addHandler(Route.Project.GET_PROJECT,    projectHandler::onGet   , true, "project.read", "user.read");
        this.apiServer.addHandler(Route.Project.LIST_PROJECTS,  projectHandler::onList  , true, "project.read");
        this.apiServer.addHandler(Route.Project.CREATE_PROJECT, projectHandler::onPost  , true, "project.read", "project.write", "user.read", "user.write");
        this.apiServer.addHandler(Route.Project.EDIT_PROJECT,   projectHandler::onPatch , true, "project.read", "project.write", "user.read", "user.write");
        this.apiServer.addHandler(Route.Project.DELETE_PROJECT, projectHandler::onDelete, true, "project.write", "user.write");

        final DiscordAccountHandler discordAccountHandler = new DiscordAccountHandler(this);
        this.apiServer.addHandler(Route.Discord.GET_DISCORD_ACCOUNT,    discordAccountHandler::onGet   , true, "discord.read", "user.read");
        this.apiServer.addHandler(Route.Discord.LIST_DISCORD_ACCOUNTS,  discordAccountHandler::onList  , true, "discord.read", "user.read");
        this.apiServer.addHandler(Route.Discord.CREATE_DISCORD_ACCOUNT, discordAccountHandler::onPost  , true, "discord.read", "discord.write", "user.read", "user.write");
        this.apiServer.addHandler(Route.Discord.EDIT_DISCORD_ACCOUNT,   discordAccountHandler::onPatch , true, "discord.read", "discord.write", "user.read", "user.write");
        this.apiServer.addHandler(Route.Discord.DELETE_DISCORD_ACCOUNT, discordAccountHandler::onDelete, true, "discord.write", "user.write");

        final MinecraftAccountHandler minecraftAccountHandler = new MinecraftAccountHandler(this);
        this.apiServer.addHandler(Route.Minecraft.GET_MINECRAFT_ACCOUNT,    minecraftAccountHandler::onGet   , true, "minecraft.read", "user.read");
        this.apiServer.addHandler(Route.Minecraft.LIST_MINECRAFT_ACCOUNTS,  minecraftAccountHandler::onList  , true, "minecraft.read", "user.read");
        this.apiServer.addHandler(Route.Minecraft.CREATE_MINECRAFT_ACCOUNT, minecraftAccountHandler::onPost  , true, "minecraft.read", "minecraft.write", "user.read", "user.write");
        this.apiServer.addHandler(Route.Minecraft.EDIT_MINECRAFT_ACCOUNT,   minecraftAccountHandler::onPatch , true, "minecraft.read", "minecraft.write", "user.read", "user.write");
        this.apiServer.addHandler(Route.Minecraft.DELETE_MINECRAFT_ACCOUNT, minecraftAccountHandler::onDelete, true, "minecraft.write", "user.write");

        this.socketServer.start();
        this.apiServer.start();
    }

    @Override
    public void stop() {
        LOG.info("Stopping...");

        this.apiServer.stop();

        try {
            this.socketServer.close();
        } catch (IOException e) {
            LOG.warn("Could not properly close SocketServer", e);
        }

        try {
            this.databaseAdapter.close();
        } catch (IOException e) {
            LOG.warn("Could not properly close DatabaseAdapter", e);
        }

        LOG.info("OK bye");
    }

    public @NotNull AuthManager getAuthManager() {
        return this.authManager;
    }

    public @NotNull DatabaseAdapter getDatabaseAdapter() {
        return this.databaseAdapter;
    }

    @Override
    public @NotNull DatabaseUserListAction retrieveUsers() {
        return new DatabaseUserListAction(this.getUsers());
    }

    @Override
    public @NotNull DatabaseTicketListAction retrieveTickets() {
        return new DatabaseTicketListAction(this.getTickets());
    }

    @Override
    public @NotNull DatabaseProjectListAction retrieveProjects() {
        return new DatabaseProjectListAction(this.getProjects());
    }

    @Override
    public @NotNull DatabaseDiscordAccountListAction retrieveDiscordAccounts() {
        return new DatabaseDiscordAccountListAction(this.getDiscordAccounts());
    }

    @Override
    public @NotNull DatabaseMinecraftAccountListAction retrieveMinecraftAccounts() {
        return new DatabaseMinecraftAccountListAction(this.getMinecraftAccounts());
    }

    @Override
    public @NotNull DatabaseUserGetAction retrieveUser(long id) {
        return new DatabaseUserGetAction(this, id);
    }

    @Override
    public @NotNull DatabaseTicketGetAction retrieveTicket(long id) {
        return new DatabaseTicketGetAction(this, id);
    }

    @Override
    public @NotNull DatabaseProjectGetAction retrieveProject(long id) {
        return new DatabaseProjectGetAction(this, id);
    }

    @Override
    public @NotNull DatabaseDiscordAccountGetAction retrieveDiscordAccount(long id) {
        return new DatabaseDiscordAccountGetAction(this, id);
    }

    @Override
    public @NotNull DatabaseMinecraftAccountGetAction retrieveMinecraftAccount(long id) {
        return new DatabaseMinecraftAccountGetAction(this, id);
    }

    @Override
    public @NotNull DatabaseUserBuilder createUser() {
        return new DatabaseUserBuilder(this);
    }

    @Override
    public @NotNull DatabaseTicketBuilder createTicket() {
        return new DatabaseTicketBuilder(this);
    }

    @Override
    public @NotNull DatabaseProjectBuilder createProject() {
        return new DatabaseProjectBuilder(this);
    }

    @Override
    public @NotNull DatabaseDiscordAccountBuilder createDiscordAccount() {
        return new DatabaseDiscordAccountBuilder(this);
    }

    @Override
    public @NotNull DatabaseMinecraftAccountBuilder createMinecraftAccount() {
        return new DatabaseMinecraftAccountBuilder(this);
    }
}
