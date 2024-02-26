package org.burrow_studios.obelisk.bruno;

import org.burrow_studios.obelisk.bruno.database.BoardDB;
import org.burrow_studios.obelisk.bruno.database.Database;
import org.burrow_studios.obelisk.bruno.net.BoardHandler;
import org.burrow_studios.obelisk.bruno.net.IssueHandler;
import org.burrow_studios.obelisk.bruno.net.TagHandler;
import org.burrow_studios.obelisk.commons.rpc.Endpoints;
import org.burrow_studios.obelisk.commons.rpc.RPCServer;
import org.burrow_studios.obelisk.commons.rpc.amqp.AMQPServer;
import org.burrow_studios.obelisk.commons.rpc.authentication.Authenticator;
import org.burrow_studios.obelisk.commons.rpc.authorization.Authorizer;
import org.burrow_studios.obelisk.commons.service.ServiceHook;
import org.burrow_studios.obelisk.commons.util.ResourceTools;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.burrow_studios.obelisk.commons.yaml.YamlUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bruno {
    private static final Logger LOG = Logger.getLogger("MAIN");

    private final @NotNull YamlSection config;
    private final @NotNull File configFile = new File(Main.DIR, "config.yaml");

    private final Database database;
    private final RPCServer<?> server;
    private final ServiceHook serviceHook;

    Bruno() throws Exception {
        ResourceTools resourceTools = ResourceTools.get(Main.class);
        YamlUtil.saveDefault(configFile, resourceTools.getResource("config.yaml"));

        this.config = YamlUtil.load(configFile, YamlSection.class);

        YamlSection dbConfig = this.config.getAsSection("db");

        LOG.log(Level.INFO, "Starting Database");
        this.database = new BoardDB(
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
                serverConfig.getAsPrimitive("queue").getAsString(),
                Authenticator.ALLOW_ALL, // The gateway client does not need to be authenticated
                Authorizer.ALLOW_ALL     // ... or authorized
        );

        final BoardHandler boardHandler = new BoardHandler(this);
        final IssueHandler issueHandler = new IssueHandler(this);
        final   TagHandler   tagHandler = new   TagHandler(this);

        this.server.addEndpoint(Endpoints.Board.GET_ALL, boardHandler::onGetAll);
        this.server.addEndpoint(Endpoints.Board.GET    , boardHandler::onGet);
        this.server.addEndpoint(Endpoints.Board.CREATE , boardHandler::onCreate);
        this.server.addEndpoint(Endpoints.Board.DELETE , boardHandler::onDelete);
        this.server.addEndpoint(Endpoints.Board.EDIT   , boardHandler::onEdit);

        this.server.addEndpoint(Endpoints.Board.Issue.GET_ALL     , issueHandler::onGetAll);
        this.server.addEndpoint(Endpoints.Board.Issue.GET         , issueHandler::onGet);
        this.server.addEndpoint(Endpoints.Board.Issue.CREATE      , issueHandler::onCreate);
        this.server.addEndpoint(Endpoints.Board.Issue.ADD_ASSIGNEE, issueHandler::onAddAssignee);
        this.server.addEndpoint(Endpoints.Board.Issue.DEL_ASSIGNEE, issueHandler::onDelAssignee);
        this.server.addEndpoint(Endpoints.Board.Issue.ADD_TAG     , issueHandler::onAddTag);
        this.server.addEndpoint(Endpoints.Board.Issue.DEL_TAG     , issueHandler::onDelTag);
        this.server.addEndpoint(Endpoints.Board.Issue.DELETE      , issueHandler::onDelete);
        this.server.addEndpoint(Endpoints.Board.Issue.EDIT        , issueHandler::onEdit);;

        this.server.addEndpoint(Endpoints.Board.Tag.GET_ALL, tagHandler::onGetAll);
        this.server.addEndpoint(Endpoints.Board.Tag.GET    , tagHandler::onGet);
        this.server.addEndpoint(Endpoints.Board.Tag.CREATE , tagHandler::onCreate);
        this.server.addEndpoint(Endpoints.Board.Tag.DELETE , tagHandler::onDelete);
        this.server.addEndpoint(Endpoints.Board.Tag.EDIT   , tagHandler::onEdit);

        this.serviceHook = new ServiceHook(serverConfig, "Bruno", this.server);
    }

    void stop() throws Exception {
        LOG.log(Level.WARNING, "Shutting down");
        this.serviceHook.close();
        this.server.close();
        this.database.close();
        this.config.save(configFile);
        LOG.log(Level.INFO, "OK bye");
    }

    public @NotNull Database getDatabase() {
        return database;
    }
}
