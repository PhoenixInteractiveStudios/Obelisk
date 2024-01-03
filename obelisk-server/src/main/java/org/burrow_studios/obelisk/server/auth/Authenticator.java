package org.burrow_studios.obelisk.server.auth;

import org.burrow_studios.obelisk.server.Main;
import org.burrow_studios.obelisk.server.ObeliskServer;
import org.burrow_studios.obelisk.server.auth.crypto.TokenManager;
import org.burrow_studios.obelisk.server.auth.db.AuthDB;
import org.burrow_studios.obelisk.server.auth.db.sqlite.SQLiteAuthDB;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Authenticator {
    private static final Logger LOG = Logger.getLogger(Authenticator.class.getSimpleName());

    private final @NotNull ObeliskServer server;
    private final @NotNull AuthDB database;
    private final @NotNull TokenManager tokenManager;

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
}
