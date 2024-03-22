package org.burrow_studios.obelisk.moderationservice;

import org.burrow_studios.obelisk.commons.rpc.Endpoints;
import org.burrow_studios.obelisk.commons.rpc.RPCServer;
import org.burrow_studios.obelisk.commons.rpc.amqp.AMQPServer;
import org.burrow_studios.obelisk.commons.service.ServiceHook;
import org.burrow_studios.obelisk.commons.util.ResourceTools;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.burrow_studios.obelisk.commons.yaml.YamlUtil;
import org.burrow_studios.obelisk.moderationservice.database.Database;
import org.burrow_studios.obelisk.moderationservice.database.ProjectDB;
import org.burrow_studios.obelisk.moderationservice.database.TicketDB;
import org.burrow_studios.obelisk.moderationservice.net.ProjectHandler;
import org.burrow_studios.obelisk.moderationservice.net.TicketHandler;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModerationService {
    private static final Logger LOG = Logger.getLogger("MAIN");

    private final @NotNull YamlSection config;
    private final @NotNull File configFile = new File(Main.DIR, "config.yaml");

    private final Database database;
    private final RPCServer<?> server;
    private final ServiceHook serviceHook;

    ModerationService() throws Exception {
        ResourceTools resourceTools = ResourceTools.get(Main.class);
        YamlUtil.saveDefault(configFile, resourceTools.getResource("config.yaml"));

        this.config = YamlUtil.load(configFile, YamlSection.class);

        YamlSection dbConfig = this.config.getAsSection("db");

        LOG.log(Level.INFO, "Starting Database");
        this.database = new Database(
                dbConfig.getAsPrimitive("host").getAsString(),
                dbConfig.getAsPrimitive("port").getAsInt(),
                dbConfig.getAsPrimitive("user").getAsString(),
                dbConfig.getAsPrimitive("pass").getAsString(),
                dbConfig.getAsPrimitive("database").getAsString()
        );

        LOG.log(Level.INFO, "Starting API server");
        YamlSection serverConfig = this.config.getAsSection("server");
        this.server = new AMQPServer(
                serverConfig.getAsPrimitive("host").getAsString(),
                serverConfig.getAsPrimitive("port").getAsInt(),
                serverConfig.getAsPrimitive("user").getAsString(),
                serverConfig.getAsPrimitive("pass").getAsString(),
                serverConfig.getAsPrimitive("exchange").getAsString(),
                serverConfig.getAsPrimitive("queue").getAsString()
        );

        final ProjectHandler projectHandler = new ProjectHandler(this);
        final  TicketHandler  ticketHandler = new  TicketHandler(this);

        this.server.addEndpoint(Endpoints.Project.GET_ALL   , projectHandler::onGetAll);
        this.server.addEndpoint(Endpoints.Project.GET       , projectHandler::onGet);
        this.server.addEndpoint(Endpoints.Project.CREATE    , projectHandler::onCreate);
        this.server.addEndpoint(Endpoints.Project.ADD_MEMBER, projectHandler::onAddMember);
        this.server.addEndpoint(Endpoints.Project.DEL_MEMBER, projectHandler::onDelMember);
        this.server.addEndpoint(Endpoints.Project.DELETE    , projectHandler::onDelete);
        this.server.addEndpoint(Endpoints.Project.EDIT      , projectHandler::onEdit);

        this.server.addEndpoint(Endpoints.Ticket.GET_ALL , ticketHandler::onGetAll);
        this.server.addEndpoint(Endpoints.Ticket.GET     , ticketHandler::onGet);
        this.server.addEndpoint(Endpoints.Ticket.CREATE  , ticketHandler::onCreate);
        this.server.addEndpoint(Endpoints.Ticket.ADD_USER, ticketHandler::onAddUser);
        this.server.addEndpoint(Endpoints.Ticket.DEL_USER, ticketHandler::onDelUser);
        this.server.addEndpoint(Endpoints.Ticket.DELETE  , ticketHandler::onDelete);
        this.server.addEndpoint(Endpoints.Ticket.EDIT    , ticketHandler::onEdit);

        this.serviceHook = new ServiceHook(serverConfig, "ModerationService", this.server);
    }

    void stop() throws Exception {
        LOG.log(Level.WARNING, "Shutting down");
        this.serviceHook.close();
        this.server.close();
        this.database.close();
        this.config.save(configFile);
        LOG.log(Level.INFO, "OK bye");
    }

    public @NotNull ProjectDB getProjectDB() {
        return database;
    }

    public @NotNull TicketDB getTicketDB() {
        return database;
    }
}
