package org.burrow_studios.obelisk.server.auth;

import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.ActionImpl;
import org.burrow_studios.obelisk.core.source.DataProvider;
import org.burrow_studios.obelisk.core.source.Request;
import org.burrow_studios.obelisk.server.Main;
import org.burrow_studios.obelisk.server.ObeliskServer;
import org.burrow_studios.obelisk.server.auth.crypto.TokenManager;
import org.burrow_studios.obelisk.server.auth.db.AuthDB;
import org.burrow_studios.obelisk.server.auth.db.SQLiteAuthDB;
import org.burrow_studios.obelisk.commons.turtle.TurtleGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Authenticator implements DataProvider {
    private static final Logger LOG = Logger.getLogger(Authenticator.class.getSimpleName());

    private final @NotNull ObeliskServer server;
    private final @NotNull AuthDB database;
    private final @NotNull TokenManager tokenManager;

    private final TurtleGenerator turtleGenerator = TurtleGenerator.get(Authenticator.class.getSimpleName());

    public Authenticator(@NotNull ObeliskServer server) throws IOException {
        this.server = server;

        try {
            LOG.log(Level.INFO, "Starting Database");
            this.database = new SQLiteAuthDB(new File(Main.DIR, "shelly.db"));

            LOG.log(Level.INFO, "Starting TokenManager");
            this.tokenManager = new TokenManager(this);
        } catch (Exception e) {
            throw new IOException("An unexpected exception was encountered when attempting to initialize the Authenticator", e);
        }
    }

    public @NotNull ObeliskServer getServer() {
        return this.server;
    }

    public @NotNull AuthDB getDatabase() {
        return this.database;
    }

    public @NotNull TokenManager getTokenManager() {
        return this.tokenManager;
    }

    @Override
    public @NotNull ObeliskImpl getAPI() {
        return this.server.getAPI();
    }

    @Override
    public @NotNull Request submitRequest(@NotNull ActionImpl<?> action) {
        throw new UnsupportedOperationException("Authenticator requests originating form the server are not supported");
    }
}
