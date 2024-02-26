package org.burrow_studios.obelisk.bruno;

import org.burrow_studios.obelisk.bruno.database.BoardDB;
import org.burrow_studios.obelisk.bruno.database.Database;
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
}
