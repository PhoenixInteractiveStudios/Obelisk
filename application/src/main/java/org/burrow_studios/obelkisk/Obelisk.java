package org.burrow_studios.obelkisk;

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

    public Obelisk() {

    }

    public void start() {
        LOG.info("Starting Obelisk...");

        this.database = new DatabaseImpl(new File(Main.DIR, "obelisk.db"));

        LOG.info("All done.");
    }

    public void stop() {
        LOG.info("Stopping...");

        try {
            this.database.close();
        } catch (IOException e) {
            LOG.warn("Could not properly close database", e);
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
