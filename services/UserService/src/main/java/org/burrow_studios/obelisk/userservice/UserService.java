package org.burrow_studios.obelisk.userservice;

import org.burrow_studios.obelisk.commons.rpc.Endpoints;
import org.burrow_studios.obelisk.commons.rpc.RPCServer;
import org.burrow_studios.obelisk.commons.rpc.amqp.AMQPServer;
import org.burrow_studios.obelisk.commons.rpc.authentication.Authenticator;
import org.burrow_studios.obelisk.commons.rpc.authorization.Authorizer;
import org.burrow_studios.obelisk.commons.service.ServiceHook;
import org.burrow_studios.obelisk.commons.util.ResourceTools;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.burrow_studios.obelisk.commons.yaml.YamlUtil;
import org.burrow_studios.obelisk.userservice.database.EntityDatabase;
import org.burrow_studios.obelisk.userservice.database.GroupDB;
import org.burrow_studios.obelisk.userservice.database.UserDB;
import org.burrow_studios.obelisk.userservice.net.GroupHandler;
import org.burrow_studios.obelisk.userservice.net.UserHandler;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {
    private static final Logger LOG = Logger.getLogger("MAIN");

    private final @NotNull YamlSection config;
    private final @NotNull File configFile = new File(Main.DIR, "config.yaml");

    private final EntityDatabase database;
    private final RPCServer<?> server;
    private final ServiceHook serviceHook;

    UserService() throws Exception {
        ResourceTools resourceTools = ResourceTools.get(Main.class);
        YamlUtil.saveDefault(configFile, resourceTools.getResource("config.yaml"));

        this.config = YamlUtil.load(configFile, YamlSection.class);

        YamlSection dbConfig = this.config.getAsSection("db");

        LOG.log(Level.INFO, "Starting Database");
        this.database = new EntityDatabase(
                dbConfig.getAsPrimitive("host").getAsString(),
                dbConfig.getAsPrimitive("port").getAsInt(),
                dbConfig.getAsPrimitive("database").getAsString(),
                dbConfig.getAsPrimitive("user").getAsString(),
                dbConfig.getAsPrimitive("pass").getAsString()
        );

        LOG.log(Level.INFO, "Starting API server");
        final GroupHandler groupHandler = new GroupHandler(this);
        final  UserHandler  userHandler = new  UserHandler(this);
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

        this.server.addEndpoint(Endpoints.Group.GET_ALL   , groupHandler::onGetAll);
        this.server.addEndpoint(Endpoints.Group.GET       , groupHandler::onGet);
        this.server.addEndpoint(Endpoints.Group.CREATE    , groupHandler::onCreate);
        this.server.addEndpoint(Endpoints.Group.ADD_MEMBER, groupHandler::onAddMember);
        this.server.addEndpoint(Endpoints.Group.DEL_MEMBER, groupHandler::onDelMember);
        this.server.addEndpoint(Endpoints.Group.DELETE    , groupHandler::onDelete);
        this.server.addEndpoint(Endpoints.Group.EDIT      , groupHandler::onEdit);

        this.server.addEndpoint(Endpoints.User.GET_ALL, userHandler::onGetAll);
        this.server.addEndpoint(Endpoints.User.GET    , userHandler::onGet);
        this.server.addEndpoint(Endpoints.User.CREATE , userHandler::onCreate);
        this.server.addEndpoint(Endpoints.User.DELETE , userHandler::onDelete);
        this.server.addEndpoint(Endpoints.User.EDIT   , userHandler::onEdit);

        this.serviceHook = new ServiceHook(serverConfig, "UserService", this.server);
    }

    void stop() throws Exception {
        LOG.log(Level.WARNING, "Shutting down");
        this.serviceHook.close();
        this.server.close();
        this.database.close();
        this.config.save(configFile);
        LOG.log(Level.INFO, "OK bye");
    }

    public @NotNull GroupDB getGroupDB() {
        return this.database;
    }

    public @NotNull UserDB getUserDB() {
        return this.database;
    }
}
