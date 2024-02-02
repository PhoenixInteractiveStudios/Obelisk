package org.burrow_studios.obelisk.shelly;

import org.burrow_studios.obelisk.commons.http.Endpoints;
import org.burrow_studios.obelisk.commons.http.server.Authorizer;
import org.burrow_studios.obelisk.commons.http.server.HTTPServer;
import org.burrow_studios.obelisk.commons.http.server.SunServerImpl;
import org.burrow_studios.obelisk.shelly.crypto.TokenManager;
import org.burrow_studios.obelisk.shelly.database.AuthDB;
import org.burrow_studios.obelisk.shelly.database.SQLiteAuthDB;
import org.burrow_studios.obelisk.shelly.net.SessionHandler;
import org.burrow_studios.obelisk.shelly.net.PubKeyHandler;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Shelly {
    private static final Logger LOG = Logger.getLogger("MAIN");

    private final AuthDB database;
    private final TokenManager tokenManager;
    private final HTTPServer server;

    Shelly() throws Exception {
        LOG.log(Level.INFO, "Starting Database");
        this.database = new SQLiteAuthDB(new File(Main.DIR, "shelly.db"));

        LOG.log(Level.INFO, "Starting TokenManager");
        this.tokenManager = new TokenManager(this);

        LOG.log(Level.INFO, "Starting API server");
        final      SessionHandler      sessionHandler = new      SessionHandler(this);
        final PubKeyHandler pubKeyHandler = new PubKeyHandler(this);
        this.server = new SunServerImpl(Authorizer.of(
                tokenManager::decodeIdentityToken,
                tokenManager::decodeSessionToken
        ), /* FIXME: placeholder */ 8348)
                .addEndpoint(Endpoints.LOGIN     , sessionHandler::onLogin)
                .addEndpoint(Endpoints.LOGOUT    , sessionHandler::onLogout)
                .addEndpoint(Endpoints.LOGOUT_ALL, sessionHandler::onLogoutAll)
                .addEndpoint(Endpoints.GET_SOCKET, sessionHandler::onGetSocket)
                .addEndpoint(Endpoints.Shelly.GET_PUBLIC_IDENTITY_KEY, pubKeyHandler::onGetPublicIdentityKey)
                .addEndpoint(Endpoints.Shelly.GET_PUBLIC_SESSION_KEY , pubKeyHandler::onGetPublicSessionKey);
    }

    public AuthDB getDatabase() {
        return database;
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public HTTPServer getServer() {
        return server;
    }

    void stop() throws Exception {
        LOG.log(Level.WARNING, "Shutting down");
        // TODO
        //server.stop();
        LOG.log(Level.INFO, "OK bye");
    }
}