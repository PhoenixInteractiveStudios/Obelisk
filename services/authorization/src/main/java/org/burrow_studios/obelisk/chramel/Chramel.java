package org.burrow_studios.obelisk.chramel;

import org.burrow_studios.obelisk.chramel.database.Database;
import org.burrow_studios.obelisk.chramel.database.SQLiteDB;
import org.burrow_studios.obelisk.chramel.net.GatewayAdapter;
import org.burrow_studios.obelisk.commons.rpc.Endpoint;
import org.burrow_studios.obelisk.commons.rpc.Method;
import org.burrow_studios.obelisk.commons.rpc.RPCServer;
import org.burrow_studios.obelisk.commons.rpc.amqp.AMQPServer;
import org.burrow_studios.obelisk.commons.rpc.authentication.AuthenticationLevel;
import org.burrow_studios.obelisk.commons.rpc.authentication.Authenticator;
import org.burrow_studios.obelisk.commons.service.ServiceHook;
import org.burrow_studios.obelisk.commons.util.ResourceTools;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.burrow_studios.obelisk.commons.yaml.YamlUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Chramel {
    private static final Logger LOG = Logger.getLogger("MAIN");

    private final @NotNull YamlSection config;
    private final @NotNull File configFile = new File(Main.DIR, "config.yaml");

    private final Database database;
    private final RPCServer<?> server;
    private final ServiceHook serviceHook;

    Chramel() throws Exception {
        ResourceTools resourceTools = ResourceTools.get(Main.class);
        YamlUtil.saveDefault(configFile, resourceTools.getResource("config.yaml"));

        this.config = YamlUtil.load(configFile, YamlSection.class);

        LOG.log(Level.INFO, "Starting Database");
        this.database = new SQLiteDB(new File(Main.DIR, "chramel.db"));

        LOG.log(Level.INFO, "Starting API server");
        GatewayAdapter gatewayAdapter = new GatewayAdapter(this);
        YamlSection serverConfig = this.config.getAsSection("server");
        this.server = new AMQPServer(
                serverConfig.getAsPrimitive("host").getAsString(),
                serverConfig.getAsPrimitive("port").getAsInt(),
                serverConfig.getAsPrimitive("user").getAsString(),
                serverConfig.getAsPrimitive("pass").getAsString(),
                serverConfig.getAsPrimitive("exchange").getAsString(),
                serverConfig.getAsPrimitive("queue").getAsString(),
                Authenticator.ALLOW_ALL // The gateway client does not need to be authenticated
        );

        this.server.addEndpoint(Endpoint.build(Method.GET, "/authorize", AuthenticationLevel.NONE), gatewayAdapter::onAuthorize);

        this.serviceHook = new ServiceHook(serverConfig, "Chramel", this.server);
    }

    void stop() throws Exception {
        LOG.log(Level.WARNING, "Shutting down");
        this.serviceHook.close();
        this.server.close();
        this.config.save(configFile);
        LOG.log(Level.INFO, "OK bye");
    }

    public @NotNull Database getDatabase() {
        return database;
    }
}
