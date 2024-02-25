package org.burrow_studios.obelisk.shelly;

import org.burrow_studios.obelisk.commons.rpc.Endpoint;
import org.burrow_studios.obelisk.commons.rpc.Endpoints;
import org.burrow_studios.obelisk.commons.rpc.Method;
import org.burrow_studios.obelisk.commons.rpc.RPCServer;
import org.burrow_studios.obelisk.commons.rpc.amqp.AMQPServer;
import org.burrow_studios.obelisk.commons.rpc.authentication.AuthenticationLevel;
import org.burrow_studios.obelisk.commons.rpc.authentication.Authenticator;
import org.burrow_studios.obelisk.commons.service.ServiceHook;
import org.burrow_studios.obelisk.commons.util.ResourceTools;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.burrow_studios.obelisk.commons.yaml.YamlUtil;
import org.burrow_studios.obelisk.shelly.crypto.TokenManager;
import org.burrow_studios.obelisk.shelly.database.AuthDB;
import org.burrow_studios.obelisk.shelly.database.SQLiteAuthDB;
import org.burrow_studios.obelisk.shelly.net.GatewayAdapter;
import org.burrow_studios.obelisk.shelly.net.PubKeyHandler;
import org.burrow_studios.obelisk.shelly.net.SessionHandler;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Shelly {
    private static final Logger LOG = Logger.getLogger("MAIN");

    private final @NotNull YamlSection config;
    private final @NotNull File configFile = new File(Main.DIR, "config.yaml");

    private final AuthDB database;
    private final TokenManager tokenManager;
    private final RPCServer<?> server;
    private final ServiceHook serviceHook;

    Shelly() throws Exception {
        ResourceTools resourceTools = ResourceTools.get(Main.class);
        YamlUtil.saveDefault(configFile, resourceTools.getResource("config.yaml"));

        this.config = YamlUtil.load(configFile, YamlSection.class);

        LOG.log(Level.INFO, "Starting Database");
        this.database = new SQLiteAuthDB(new File(Main.DIR, "shelly.db"));

        LOG.log(Level.INFO, "Starting TokenManager");
        this.tokenManager = new TokenManager(this);

        LOG.log(Level.INFO, "Starting API server");
        final GatewayAdapter gatewayAdapter = new GatewayAdapter(this);
        final SessionHandler sessionHandler = new SessionHandler(this);
        final  PubKeyHandler  pubKeyHandler = new PubKeyHandler(this);
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

        this.server.addEndpoint(Endpoints.LOGIN     , sessionHandler::onLogin);
        this.server.addEndpoint(Endpoints.LOGOUT    , sessionHandler::onLogout);
        this.server.addEndpoint(Endpoints.LOGOUT_ALL, sessionHandler::onLogoutAll);
        this.server.addEndpoint(Endpoints.GET_SOCKET, sessionHandler::onGetSocket);
        this.server.addEndpoint(Endpoints.Shelly.GET_PUBLIC_IDENTITY_KEY, pubKeyHandler::onGetPublicIdentityKey);
        this.server.addEndpoint(Endpoints.Shelly.GET_PUBLIC_SESSION_KEY , pubKeyHandler::onGetPublicSessionKey);
        this.server.addEndpoint(Endpoint.build(Method.GET, "/authenticate", AuthenticationLevel.NONE), gatewayAdapter::onAuthenticate);

        this.serviceHook = new ServiceHook(serverConfig, "Shelly", this.server);
    }

    public AuthDB getDatabase() {
        return database;
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public RPCServer<?> getServer() {
        return server;
    }

    void stop() throws Exception {
        LOG.log(Level.WARNING, "Shutting down");
        this.serviceHook.close();
        this.server.close();
        this.config.save(configFile);
        LOG.log(Level.INFO, "OK bye");
    }
}