package org.burrow_studios.obelisk.monolith;

import org.burrow_studios.obelisk.core.AbstractObelisk;
import org.burrow_studios.obelisk.core.http.Route;
import org.burrow_studios.obelisk.monolith.action.entity.discord.DatabaseDiscordAccountBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.DatabaseMinecraftAccountBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.project.DatabaseProjectBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.ticket.DatabaseTicketBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.user.DatabaseUserBuilder;
import org.burrow_studios.obelisk.monolith.db.DatabaseAdapter;
import org.burrow_studios.obelisk.monolith.db.impl.ActionableDatabase;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.burrow_studios.obelisk.monolith.http.HTTPServer;
import org.burrow_studios.obelisk.monolith.http.handlers.*;
import org.burrow_studios.obelisk.util.EnvUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ObeliskMonolith extends AbstractObelisk {
    private static final Logger LOG = LoggerFactory.getLogger(ObeliskMonolith.class);

    private final DatabaseAdapter databaseAdapter;
    private final HTTPServer apiServer;

    public ObeliskMonolith() throws DatabaseException, IOException {
        super();

        this.databaseAdapter = new DatabaseAdapter();
        this.databaseAdapter.registerDatabase(new ActionableDatabase(new File(Main.DIR, "entities.db")));

        this.apiServer = new HTTPServer(EnvUtil.getInt("API_PORT", 80));

        final UserHandler userHandler = new UserHandler(this);
        this.apiServer.addHandler(Route.User.GET_USER, userHandler::onGet);

        final TicketHandler ticketHandler = new TicketHandler(this);
        this.apiServer.addHandler(Route.Ticket.GET_TICKET, ticketHandler::onGet);

        final ProjectHandler projectHandler = new ProjectHandler(this);
        this.apiServer.addHandler(Route.Project.GET_PROJECT, projectHandler::onGet);

        final DiscordAccountHandler discordAccountHandler = new DiscordAccountHandler(this);
        this.apiServer.addHandler(Route.Discord.GET_DISCORD_ACCOUNT, discordAccountHandler::onGet);

        final MinecraftAccountHandler minecraftAccountHandler = new MinecraftAccountHandler(this);
        this.apiServer.addHandler(Route.Minecraft.GET_MINECRAFT_ACCOUNT, minecraftAccountHandler::onGet);

        this.apiServer.start();
    }

    public @NotNull DatabaseAdapter getDatabaseAdapter() {
        return this.databaseAdapter;
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
