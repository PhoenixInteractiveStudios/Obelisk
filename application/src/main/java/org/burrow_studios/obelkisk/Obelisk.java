package org.burrow_studios.obelkisk;

import org.burrow_studios.obelisk.util.ResourceTools;
import org.burrow_studios.obelkisk.db.DatabaseImpl;
import org.burrow_studios.obelkisk.entity.DiscordAccount;
import org.burrow_studios.obelkisk.entity.Ticket;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Obelisk {
    private static final Logger LOG = LoggerFactory.getLogger(Obelisk.class);

    private DatabaseImpl database;
    private Config config;

    public Obelisk() { }

    public void start() throws IOException {
        LOG.info("Starting Obelisk...");

        LOG.debug("Creating default config file");
        ResourceTools.get(Main.class).createDefault(Main.DIR, "config.properties");

        LOG.debug("Reading config");
        this.config = Config.fromFile(new File(Main.DIR, "config.properties"));


        LOG.info("Initializing database");
        this.database = new DatabaseImpl(new File(Main.DIR, "obelisk.db"));

        LOG.info("All done.");
    }

    public void stop() {
        LOG.info("Stopping...");

        if (this.database != null) {
            try {
                this.database.close();
            } catch (IOException e) {
                LOG.warn("Could not properly close database", e);
            }
            this.database = null;
        }

        LOG.info("OK bye");
    }

    public @NotNull Ticket createTicket(@NotNull String name) {
        return this.database.createTicket(name);
    }

    public @NotNull DiscordAccount createDiscordAccount(long snowflake, @NotNull String name) {
        return this.database.createDiscordAccount(snowflake, name);
    }
}
